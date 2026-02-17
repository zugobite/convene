package com.zugobite.convene.controllers;

import com.zugobite.convene.data.DataPersistence;
import com.zugobite.convene.models.Event;
import com.zugobite.convene.models.Staff;
import com.zugobite.convene.services.EventManager;
import com.zugobite.convene.utils.ConsoleUtils;
import com.zugobite.convene.utils.InputValidator;

import java.util.List;
import java.util.Scanner;

/**
 * Handles the staff menu loop and dispatches actions to the appropriate
 * service methods. Each action is guarded by a permission check to enforce
 * role-based access control.
 *
 * <p>Menu options:</p>
 * <ol>
 *   <li>Create Event</li>
 *   <li>Update Event</li>
 *   <li>Cancel Event</li>
 *   <li>View Participants &amp; Waitlists</li>
 *   <li>Search Events</li>
 *   <li>Exit</li>
 * </ol>
 *
 * @author Zascia Hugo
 * @version 0.4.0
 * @see Staff
 * @see EventManager
 */
public class StaffMenuController {

    /** The staff user associated with this menu session. */
    private final Staff staff;

    /** The shared event manager for all event operations. */
    private final EventManager eventManager;

    /** The persistence layer for saving data on modification. */
    private final DataPersistence persistence;

    /**
     * Constructs a StaffMenuController for the given staff member.
     *
     * @param staff        the authenticated staff user
     * @param eventManager the shared event manager instance
     * @param persistence  the data persistence instance for auto-save
     */
    public StaffMenuController(Staff staff, EventManager eventManager, DataPersistence persistence) {
        this.staff = staff;
        this.eventManager = eventManager;
        this.persistence = persistence;
    }

    /**
     * Runs the staff menu loop. Continuously displays the menu, reads
     * the user's choice, and dispatches to the corresponding action.
     * Exits when the user selects option 6.
     *
     * @param scanner the Scanner instance for reading console input
     */
    public void run(Scanner scanner) {
        boolean running = true;

        while (running) {
            staff.showMenu();
            String input = scanner.nextLine().trim();

            if (!InputValidator.isValidMenuChoice(input, 6)) {
                ConsoleUtils.printError("Invalid choice. Please enter a number between 1 and 6.");
                continue;
            }

            int choice = Integer.parseInt(input);

            switch (choice) {
                case 1 -> createEvent(scanner);
                case 2 -> updateEvent(scanner);
                case 3 -> cancelEvent(scanner);
                case 4 -> viewParticipants(scanner);
                case 5 -> searchEvents(scanner);
                case 6 -> running = false;
                default -> ConsoleUtils.printError("Invalid choice.");
            }

            if (running) {
                ConsoleUtils.pressEnterToContinue(scanner);
            }
        }
    }

    // ---- Section 2.2 — Event Management Actions ----

    /**
     * Creates a new event by prompting the staff user for all required fields.
     * Validates each field before submission. Displays the created event on success.
     *
     * @param scanner the Scanner instance for reading input
     */
    private void createEvent(Scanner scanner) {
        if (!staff.hasPermission("CREATE_EVENT")) {
            ConsoleUtils.printError("Access denied: Staff cannot perform this action.");
            return;
        }

        ConsoleUtils.printHeader("Create New Event");

        // Event ID
        String idInput;
        do {
            System.out.print("Enter Event ID (positive integer): ");
            idInput = scanner.nextLine().trim();
            if (!InputValidator.isPositiveInteger(idInput)) {
                ConsoleUtils.printError("Event ID must be a positive integer.");
            } else if (eventManager.eventExists(Integer.parseInt(idInput))) {
                ConsoleUtils.printError("Event ID " + idInput + " already exists. Choose a different ID.");
                idInput = ""; // Force re-prompt
            }
        } while (!InputValidator.isPositiveInteger(idInput) || eventManager.eventExists(Integer.parseInt(idInput)));
        int eventId = Integer.parseInt(idInput);

        // Event Name
        String eventName;
        do {
            System.out.print("Enter Event Name: ");
            eventName = scanner.nextLine().trim();
            if (!InputValidator.isNonEmpty(eventName)) {
                ConsoleUtils.printError("Event name cannot be empty.");
            }
        } while (!InputValidator.isNonEmpty(eventName));

        // Event Date
        String eventDate;
        do {
            System.out.print("Enter Event Date (dd/mm/yyyy): ");
            eventDate = scanner.nextLine().trim();
            if (!InputValidator.isValidDate(eventDate)) {
                ConsoleUtils.printError("Invalid date. Please use dd/mm/yyyy format.");
            }
        } while (!InputValidator.isValidDate(eventDate));

        // Event Time
        String eventTime;
        do {
            System.out.print("Enter Event Time (HH:mm): ");
            eventTime = scanner.nextLine().trim();
            if (!InputValidator.isValidTime(eventTime)) {
                ConsoleUtils.printError("Invalid time. Please use HH:mm 24-hour format.");
            }
        } while (!InputValidator.isValidTime(eventTime));

        // Location
        String location;
        do {
            System.out.print("Enter Location: ");
            location = scanner.nextLine().trim();
            if (!InputValidator.isNonEmpty(location)) {
                ConsoleUtils.printError("Location cannot be empty.");
            }
        } while (!InputValidator.isNonEmpty(location));

        // Max Participants
        String maxInput;
        do {
            System.out.print("Enter Maximum Participants: ");
            maxInput = scanner.nextLine().trim();
            if (!InputValidator.isPositiveInteger(maxInput)) {
                ConsoleUtils.printError("Maximum participants must be a positive integer.");
            }
        } while (!InputValidator.isPositiveInteger(maxInput));
        int maxParticipants = Integer.parseInt(maxInput);

        // Create the event
        Event event = eventManager.createEvent(eventId, eventName, eventDate, eventTime, location, maxParticipants);

        if (event != null) {
            System.out.println();
            ConsoleUtils.printSuccess("Event created successfully!");
            ConsoleUtils.printDivider();
            System.out.println(event.toDetailedString());
            ConsoleUtils.printDivider();
            persistence.saveData(eventManager);
        } else {
            ConsoleUtils.printError("Failed to create event. ID may already exist.");
        }
    }

    /**
     * Updates an existing event's name, time, or location.
     * Prompts for the event ID, displays current details, then offers
     * a sub-menu to update individual fields.
     *
     * @param scanner the Scanner instance for reading input
     */
    private void updateEvent(Scanner scanner) {
        if (!staff.hasPermission("UPDATE_EVENT")) {
            ConsoleUtils.printError("Access denied: Staff cannot perform this action.");
            return;
        }

        ConsoleUtils.printHeader("Update Event");

        if (eventManager.getActiveEventCount() == 0) {
            ConsoleUtils.printInfo("No active events to update.");
            return;
        }

        // Show active events for reference
        displayEventList(eventManager.getActiveEvents(), "Active Events");

        // Prompt for event ID
        System.out.print("Enter Event ID to update: ");
        String idInput = scanner.nextLine().trim();

        if (!InputValidator.isPositiveInteger(idInput)) {
            ConsoleUtils.printError("Invalid Event ID.");
            return;
        }

        int eventId = Integer.parseInt(idInput);
        Event event = eventManager.getEvent(eventId);

        if (event == null) {
            ConsoleUtils.printError("Event with ID " + eventId + " not found.");
            return;
        }

        if (event.isCancelled()) {
            ConsoleUtils.printError("Cannot update a cancelled event.");
            return;
        }

        // Display current details
        ConsoleUtils.printDivider();
        System.out.println("Current event details:");
        System.out.println(event.toDetailedString());
        ConsoleUtils.printDivider();

        // Update sub-menu
        System.out.println("What would you like to update?");
        System.out.println("  1. Event Name");
        System.out.println("  2. Event Time");
        System.out.println("  3. Location");
        System.out.println("  4. Cancel (go back)");
        System.out.print("Your choice: ");

        String updateChoice = scanner.nextLine().trim();

        if (!InputValidator.isValidMenuChoice(updateChoice, 4)) {
            ConsoleUtils.printError("Invalid choice.");
            return;
        }

        int updateOption = Integer.parseInt(updateChoice);

        switch (updateOption) {
            case 1 -> {
                String newName;
                do {
                    System.out.print("Enter new Event Name: ");
                    newName = scanner.nextLine().trim();
                    if (!InputValidator.isNonEmpty(newName)) {
                        ConsoleUtils.printError("Event name cannot be empty.");
                    }
                } while (!InputValidator.isNonEmpty(newName));
                event.setEventName(newName);
                ConsoleUtils.printSuccess("Event name updated to: " + newName);
                persistence.saveData(eventManager);
            }
            case 2 -> {
                String newTime;
                do {
                    System.out.print("Enter new Event Time (HH:mm): ");
                    newTime = scanner.nextLine().trim();
                    if (!InputValidator.isValidTime(newTime)) {
                        ConsoleUtils.printError("Invalid time. Please use HH:mm 24-hour format.");
                    }
                } while (!InputValidator.isValidTime(newTime));
                event.setEventTime(newTime);
                ConsoleUtils.printSuccess("Event time updated to: " + newTime);
                persistence.saveData(eventManager);
            }
            case 3 -> {
                String newLocation;
                do {
                    System.out.print("Enter new Location: ");
                    newLocation = scanner.nextLine().trim();
                    if (!InputValidator.isNonEmpty(newLocation)) {
                        ConsoleUtils.printError("Location cannot be empty.");
                    }
                } while (!InputValidator.isNonEmpty(newLocation));
                event.setLocation(newLocation);
                ConsoleUtils.printSuccess("Event location updated to: " + newLocation);
                persistence.saveData(eventManager);
            }
            case 4 -> ConsoleUtils.printInfo("Update cancelled.");
        }
    }

    /**
     * Cancels an existing event after confirmation.
     * The event is marked as cancelled but retained in the system for history.
     *
     * @param scanner the Scanner instance for reading input
     */
    private void cancelEvent(Scanner scanner) {
        if (!staff.hasPermission("CANCEL_EVENT")) {
            ConsoleUtils.printError("Access denied: Staff cannot perform this action.");
            return;
        }

        ConsoleUtils.printHeader("Cancel Event");

        if (eventManager.getActiveEventCount() == 0) {
            ConsoleUtils.printInfo("No active events to cancel.");
            return;
        }

        // Show active events for reference
        displayEventList(eventManager.getActiveEvents(), "Active Events");

        // Prompt for event ID
        System.out.print("Enter Event ID to cancel: ");
        String idInput = scanner.nextLine().trim();

        if (!InputValidator.isPositiveInteger(idInput)) {
            ConsoleUtils.printError("Invalid Event ID.");
            return;
        }

        int eventId = Integer.parseInt(idInput);
        Event event = eventManager.getEvent(eventId);

        if (event == null) {
            ConsoleUtils.printError("Event with ID " + eventId + " not found.");
            return;
        }

        if (event.isCancelled()) {
            ConsoleUtils.printError("Event is already cancelled.");
            return;
        }

        // Confirmation
        System.out.println("You are about to cancel: " + event.getEventName());
        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes") || confirm.equals("y")) {
            boolean success = eventManager.cancelEvent(eventId);
            if (success) {
                ConsoleUtils.printSuccess("Event \"" + event.getEventName() + "\" has been cancelled.");
                persistence.saveData(eventManager);
            } else {
                ConsoleUtils.printError("Failed to cancel the event.");
            }
        } else {
            ConsoleUtils.printInfo("Cancellation aborted.");
        }
    }

    /**
     * Displays registered participants and waitlist for a specific event,
     * or provides a summary across all events. Includes sort options.
     *
     * @param scanner the Scanner instance for reading input
     */
    private void viewParticipants(Scanner scanner) {
        if (!staff.hasPermission("VIEW_PARTICIPANTS")) {
            ConsoleUtils.printError("Access denied: Staff cannot perform this action.");
            return;
        }

        ConsoleUtils.printHeader("View Participants & Waitlists");

        if (eventManager.getTotalEventCount() == 0) {
            ConsoleUtils.printInfo("No events in the system.");
            return;
        }

        // Sort options
        System.out.println("Sort events by:");
        System.out.println("  1. Event Name (alphabetical)");
        System.out.println("  2. Event Date (chronological)");
        System.out.println("  3. Default order");
        System.out.print("Your choice: ");

        String sortChoice = scanner.nextLine().trim();
        List<Event> events;

        if ("1".equals(sortChoice)) {
            events = eventManager.getEventsSortedByName();
        } else if ("2".equals(sortChoice)) {
            events = eventManager.getEventsSortedByDate();
        } else {
            events = eventManager.getAllEvents();
        }

        if (events.isEmpty()) {
            ConsoleUtils.printInfo("No events to display.");
            return;
        }

        // Display each event with participants and waitlist
        for (Event event : events) {
            ConsoleUtils.printDivider();
            System.out.println(event.toDetailedString());

            // Registered participants
            List<String> registered = event.getRegisteredParticipants();
            if (registered.isEmpty()) {
                System.out.println("  Registered  : (none)");
            } else {
                System.out.println("  Registered  :");
                for (int i = 0; i < registered.size(); i++) {
                    System.out.println("    " + (i + 1) + ". " + registered.get(i));
                }
            }

            // Waitlist
            List<String> waitlisted = event.getWaitlist();
            if (waitlisted.isEmpty()) {
                System.out.println("  Waitlist    : (none)");
            } else {
                System.out.println("  Waitlist    :");
                for (int i = 0; i < waitlisted.size(); i++) {
                    System.out.println("    " + (i + 1) + ". " + waitlisted.get(i));
                }
            }
        }
        ConsoleUtils.printDivider();
        ConsoleUtils.printInfo("Total: " + eventManager.getActiveEventCount() + " active / "
                + eventManager.getTotalEventCount() + " total events.");
    }

    /**
     * Searches for events by name (partial/full match) or date (exact match).
     * Displays full event details including registered count and waitlist count.
     *
     * @param scanner the Scanner instance for reading input
     */
    private void searchEvents(Scanner scanner) {
        if (!staff.hasPermission("SEARCH_EVENTS")) {
            ConsoleUtils.printError("Access denied: Staff cannot perform this action.");
            return;
        }

        ConsoleUtils.printHeader("Search Events");

        if (eventManager.getTotalEventCount() == 0) {
            ConsoleUtils.printInfo("No events in the system to search.");
            return;
        }

        System.out.println("Search by:");
        System.out.println("  1. Event Name (partial or full match)");
        System.out.println("  2. Event Date (exact match, dd/mm/yyyy)");
        System.out.print("Your choice: ");

        String searchChoice = scanner.nextLine().trim();
        List<Event> results;

        if ("1".equals(searchChoice)) {
            System.out.print("Enter event name to search: ");
            String query = scanner.nextLine().trim();
            if (!InputValidator.isNonEmpty(query)) {
                ConsoleUtils.printError("Search term cannot be empty.");
                return;
            }
            results = eventManager.searchByName(query);
        } else if ("2".equals(searchChoice)) {
            System.out.print("Enter event date (dd/mm/yyyy): ");
            String date = scanner.nextLine().trim();
            if (!InputValidator.isValidDate(date)) {
                ConsoleUtils.printError("Invalid date format. Please use dd/mm/yyyy.");
                return;
            }
            results = eventManager.searchByDate(date);
        } else {
            ConsoleUtils.printError("Invalid choice.");
            return;
        }

        if (results.isEmpty()) {
            ConsoleUtils.printInfo("No events found matching your search.");
        } else {
            ConsoleUtils.printDivider();
            System.out.println("  Found " + results.size() + " event(s):");
            ConsoleUtils.printDivider();
            for (Event event : results) {
                System.out.println(event.toDetailedString());
                ConsoleUtils.printDivider();
            }
        }
    }

    // ---- Helper Methods ----

    /**
     * Displays a formatted list of events with a title header.
     *
     * @param events the list of events to display
     * @param title  the heading for the list
     */
    private void displayEventList(List<Event> events, String title) {
        ConsoleUtils.printDivider();
        System.out.println("  " + title + ":");
        ConsoleUtils.printDivider();
        for (Event event : events) {
            System.out.println("  " + event);
        }
        ConsoleUtils.printDivider();
        System.out.println();
    }
}
