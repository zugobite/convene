package com.zugobite.convene.data;

import com.zugobite.convene.models.Event;
import com.zugobite.convene.services.EventManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Handles file-based data persistence for the Campus Event Management System.
 * Saves and loads events, registrations, and waitlists to/from a plain-text
 * {@code .txt} file in a human-readable, pipe-delimited format.
 *
 * <h3>File Format</h3>
 * <p>The data file uses a record-per-line format with three record types:</p>
 * <pre>
 * EVENT|id|name|date|time|location|maxParticipants|cancelled
 * REG|eventId|userId
 * WAIT|eventId|userId
 * </pre>
 *
 * <p>Events are written first, followed by all registrations, then all
 * waitlist entries. This ensures events exist before participants are
 * added during the load phase.</p>
 *
 * <p>Demonstrates:</p>
 * <ul>
 *   <li><b>File I/O</b> — {@code BufferedReader}/{@code BufferedWriter} for efficient I/O</li>
 *   <li><b>Exception handling</b> — graceful recovery from missing/corrupt files</li>
 *   <li><b>Data serialisation</b> — structured text format for domain objects</li>
 * </ul>
 *
 * @author Zascia Hugo
 * @version 0.4.0
 * @see EventManager
 * @see Event
 */
public class DataPersistence {

    /** Default directory for data files. */
    private static final String DATA_DIR = "data";

    /** Default filename for the persisted data file. */
    private static final String DATA_FILE = "convene_data.txt";

    /** The full path to the data file. */
    private final String filePath;

    /**
     * Constructs a DataPersistence instance using the default data directory
     * and filename ({@code data/convene_data.txt}).
     */
    public DataPersistence() {
        this.filePath = DATA_DIR + File.separator + DATA_FILE;
    }

    /**
     * Constructs a DataPersistence instance with a custom file path.
     *
     * @param filePath the full path to the data file
     */
    public DataPersistence(String filePath) {
        this.filePath = filePath;
    }

    // ---- Save Operations ----

    /**
     * Saves all events, registrations, and waitlists from the given
     * {@link EventManager} to the data file. Overwrites any existing file.
     *
     * <p>Write order: all EVENT records, then all REG records, then
     * all WAIT records. This ensures correct load ordering.</p>
     *
     * @param eventManager the event manager containing data to persist
     * @return {@code true} if the save was successful, {@code false} on I/O error
     */
    public boolean saveData(EventManager eventManager) {
        // Ensure the data directory exists
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            Map<Integer, Event> events = eventManager.getEventsMap();

            for (Event event : events.values()) {
                // EVENT|id|name|date|time|location|maxParticipants|cancelled
                writer.write(String.format("EVENT|%d|%s|%s|%s|%s|%d|%b",
                        event.getEventId(),
                        escapeField(event.getEventName()),
                        event.getEventDate(),
                        event.getEventTime(),
                        escapeField(event.getLocation()),
                        event.getMaxParticipants(),
                        event.isCancelled()));
                writer.newLine();

                // REG|eventId|userId
                for (String userId : event.getRegisteredParticipants()) {
                    writer.write(String.format("REG|%d|%s",
                            event.getEventId(), userId));
                    writer.newLine();
                }

                // WAIT|eventId|userId (in queue order)
                for (String userId : event.getWaitlist()) {
                    writer.write(String.format("WAIT|%d|%s",
                            event.getEventId(), userId));
                    writer.newLine();
                }
            }

            return true;

        } catch (IOException e) {
            System.err.println("[DataPersistence] Error saving data: " + e.getMessage());
            return false;
        }
    }

    // ---- Load Operations ----

    /**
     * Loads events, registrations, and waitlists from the data file into
     * the given {@link EventManager}. Skips malformed lines with a warning.
     *
     * <p>If the data file does not exist, this method returns {@code true}
     * (fresh start with no data to load).</p>
     *
     * @param eventManager the event manager to populate with saved data
     * @return {@code true} if the load was successful or no file exists,
     *         {@code false} on I/O error
     */
    public boolean loadData(EventManager eventManager) {
        File file = new File(filePath);

        if (!file.exists()) {
            return true; // No saved data — fresh start
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) {
                    continue; // Skip blank lines
                }

                String[] parts = line.split("(?<!\\\\)\\|", -1);

                if (parts.length == 0) {
                    continue;
                }

                switch (parts[0]) {
                    case "EVENT" -> loadEvent(parts, eventManager, lineNumber);
                    case "REG"   -> loadRegistration(parts, eventManager, lineNumber);
                    case "WAIT"  -> loadWaitlistEntry(parts, eventManager, lineNumber);
                    default -> System.err.println("[DataPersistence] Warning: unknown record type '"
                            + parts[0] + "' on line " + lineNumber + " — skipping.");
                }
            }

            return true;

        } catch (IOException e) {
            System.err.println("[DataPersistence] Error loading data: " + e.getMessage());
            return false;
        }
    }

    // ---- Private Helpers ----

    /**
     * Parses an EVENT record and adds it to the EventManager.
     * Format: EVENT|id|name|date|time|location|maxParticipants|cancelled
     */
    private void loadEvent(String[] parts, EventManager eventManager, int lineNumber) {
        if (parts.length < 8) {
            System.err.println("[DataPersistence] Warning: malformed EVENT on line "
                    + lineNumber + " — skipping.");
            return;
        }

        try {
            int eventId = Integer.parseInt(parts[1]);
            String name = unescapeField(parts[2]);
            String date = parts[3];
            String time = parts[4];
            String location = unescapeField(parts[5]);
            int maxParticipants = Integer.parseInt(parts[6]);
            boolean cancelled = Boolean.parseBoolean(parts[7]);

            Event event = new Event(eventId, name, date, time, location, maxParticipants);
            event.setCancelled(cancelled);
            eventManager.addEvent(event);

        } catch (NumberFormatException e) {
            System.err.println("[DataPersistence] Warning: invalid number in EVENT on line "
                    + lineNumber + " — skipping.");
        }
    }

    /**
     * Parses a REG record and registers the user for the event.
     * Format: REG|eventId|userId
     */
    private void loadRegistration(String[] parts, EventManager eventManager, int lineNumber) {
        if (parts.length < 3) {
            System.err.println("[DataPersistence] Warning: malformed REG on line "
                    + lineNumber + " — skipping.");
            return;
        }

        try {
            int eventId = Integer.parseInt(parts[1]);
            String userId = parts[2];

            Event event = eventManager.getEvent(eventId);
            if (event != null) {
                event.addParticipant(userId);
            } else {
                System.err.println("[DataPersistence] Warning: REG references unknown event "
                        + eventId + " on line " + lineNumber + " — skipping.");
            }
        } catch (NumberFormatException e) {
            System.err.println("[DataPersistence] Warning: invalid event ID in REG on line "
                    + lineNumber + " — skipping.");
        }
    }

    /**
     * Parses a WAIT record and adds the user to the event's waitlist.
     * Format: WAIT|eventId|userId
     */
    private void loadWaitlistEntry(String[] parts, EventManager eventManager, int lineNumber) {
        if (parts.length < 3) {
            System.err.println("[DataPersistence] Warning: malformed WAIT on line "
                    + lineNumber + " — skipping.");
            return;
        }

        try {
            int eventId = Integer.parseInt(parts[1]);
            String userId = parts[2];

            Event event = eventManager.getEvent(eventId);
            if (event != null) {
                event.addToWaitlist(userId);
            } else {
                System.err.println("[DataPersistence] Warning: WAIT references unknown event "
                        + eventId + " on line " + lineNumber + " — skipping.");
            }
        } catch (NumberFormatException e) {
            System.err.println("[DataPersistence] Warning: invalid event ID in WAIT on line "
                    + lineNumber + " — skipping.");
        }
    }

    /**
     * Escapes pipe characters in a field value to prevent parsing issues.
     * Replaces {@code |} with {@code \\|} for safe storage.
     *
     * @param value the original field value
     * @return the escaped value
     */
    private String escapeField(String value) {
        return value.replace("|", "\\|");
    }

    /**
     * Unescapes pipe characters in a stored field value.
     * Replaces {@code \\|} back to {@code |}.
     *
     * @param value the escaped field value
     * @return the original value
     */
    private String unescapeField(String value) {
        return value.replace("\\|", "|");
    }

    /**
     * Returns the file path used for data persistence.
     *
     * @return the data file path
     */
    public String getFilePath() {
        return filePath;
    }
}
