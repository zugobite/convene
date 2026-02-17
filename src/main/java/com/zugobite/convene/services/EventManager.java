package com.zugobite.convene.services;

import com.zugobite.convene.models.Event;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class managing all event operations in the Campus Event Management System.
 * Acts as the central repository for events and provides business logic for
 * creating, updating, cancelling, listing, sorting, and registration management.
 *
 * <p>Demonstrates:</p>
 * <ul>
 *   <li><b>Collections</b> — {@code HashMap} for O(1) event lookup by ID</li>
 *   <li><b>Multithreading</b> — background thread for automated waitlist promotion</li>
 *   <li><b>Modular design</b> — reusable methods for all event operations</li>
 *   <li><b>Encapsulation</b> — events stored privately, accessed through methods</li>
 * </ul>
 *
 * @author Zascia Hugo
 * @version 0.4.0
 * @see Event
 */
public class EventManager {

    /** Map of event ID to Event object for fast lookup. */
    private final Map<Integer, Event> events;

    /**
     * Constructs a new EventManager with an empty event store.
     */
    public EventManager() {
        this.events = new HashMap<>();
    }

    // ---- CRUD Operations ----

    /**
     * Creates a new event and adds it to the store.
     *
     * @param eventId         the unique event ID
     * @param eventName       the name of the event
     * @param eventDate       the date in dd/mm/yyyy format
     * @param eventTime       the time in HH:mm format
     * @param location        the event location
     * @param maxParticipants the maximum number of participants
     * @return the created {@link Event}, or {@code null} if the ID already exists
     */
    public Event createEvent(int eventId, String eventName, String eventDate,
                             String eventTime, String location, int maxParticipants) {
        if (events.containsKey(eventId)) {
            return null; // Duplicate ID
        }
        Event event = new Event(eventId, eventName, eventDate, eventTime, location, maxParticipants);
        events.put(eventId, event);
        return event;
    }

    /**
     * Retrieves an event by its ID.
     *
     * @param eventId the event ID to look up
     * @return the {@link Event} if found, or {@code null}
     */
    public Event getEvent(int eventId) {
        return events.get(eventId);
    }

    /**
     * Checks whether an event with the given ID exists.
     *
     * @param eventId the event ID to check
     * @return {@code true} if the event exists
     */
    public boolean eventExists(int eventId) {
        return events.containsKey(eventId);
    }

    /**
     * Cancels an event by marking it as cancelled.
     * Does not remove it from the store so history is preserved.
     *
     * @param eventId the event ID to cancel
     * @return {@code true} if the event was successfully cancelled,
     *         {@code false} if not found or already cancelled
     */
    public boolean cancelEvent(int eventId) {
        Event event = events.get(eventId);
        if (event == null || event.isCancelled()) {
            return false;
        }
        event.setCancelled(true);
        return true;
    }

    // ---- Listing and Sorting ----

    /**
     * Returns all active (non-cancelled) events as a list.
     *
     * @return list of active events
     */
    public List<Event> getActiveEvents() {
        List<Event> active = new ArrayList<>();
        for (Event event : events.values()) {
            if (!event.isCancelled()) {
                active.add(event);
            }
        }
        return active;
    }

    /**
     * Returns all events (including cancelled) as a list.
     *
     * @return list of all events
     */
    public List<Event> getAllEvents() {
        return new ArrayList<>(events.values());
    }

    /**
     * Returns all active events sorted by event name (alphabetical, case-insensitive).
     *
     * @return sorted list of active events
     */
    public List<Event> getEventsSortedByName() {
        List<Event> active = getActiveEvents();
        active.sort(Comparator.comparing(e -> e.getEventName().toLowerCase()));
        return active;
    }

    /**
     * Returns all active events sorted by event date (chronological).
     * Dates are in dd/mm/yyyy format, so we parse and compare them properly.
     *
     * @return sorted list of active events
     */
    public List<Event> getEventsSortedByDate() {
        List<Event> active = getActiveEvents();
        active.sort((e1, e2) -> {
            String[] parts1 = e1.getEventDate().split("/");
            String[] parts2 = e2.getEventDate().split("/");
            // Compare year, then month, then day
            int yearCmp = parts1[2].compareTo(parts2[2]);
            if (yearCmp != 0) return yearCmp;
            int monthCmp = parts1[1].compareTo(parts2[1]);
            if (monthCmp != 0) return monthCmp;
            return parts1[0].compareTo(parts2[0]);
        });
        return active;
    }

    /**
     * Returns the total number of events (including cancelled).
     *
     * @return the total event count
     */
    public int getTotalEventCount() {
        return events.size();
    }

    /**
     * Returns the number of active (non-cancelled) events.
     *
     * @return the active event count
     */
    public int getActiveEventCount() {
        return (int) events.values().stream().filter(e -> !e.isCancelled()).count();
    }

    // ---- Registration Operations (Section 2.3) ----

    /**
     * Registers a student for an event. If the event has space, the student
     * is added to the registered participants list. If the event is full,
     * the student is automatically added to the waitlist queue.
     *
     * @param eventId  the event ID to register for
     * @param userId   the student's user ID
     * @return a status string: "REGISTERED", "WAITLISTED", "DUPLICATE",
     *         "CANCELLED", or "NOT_FOUND"
     */
    public String registerStudent(int eventId, String userId) {
        Event event = events.get(eventId);
        if (event == null) {
            return "NOT_FOUND";
        }
        if (event.isCancelled()) {
            return "CANCELLED";
        }
        if (event.isRegistered(userId) || event.isWaitlisted(userId)) {
            return "DUPLICATE";
        }
        if (event.hasSpace()) {
            event.addParticipant(userId);
            return "REGISTERED";
        } else {
            event.addToWaitlist(userId);
            return "WAITLISTED";
        }
    }

    /**
     * Cancels a student's registration or waitlist entry for an event.
     * If the student was registered and the waitlist is non-empty,
     * the first waitlisted student is promoted automatically in a
     * <b>separate background thread</b>.
     *
     * @param eventId  the event ID to cancel from
     * @param userId   the student's user ID
     * @return a status string: "CANCELLED_REG", "CANCELLED_WAIT",
     *         "NOT_REGISTERED", "EVENT_CANCELLED", or "NOT_FOUND"
     */
    public String cancelRegistration(int eventId, String userId) {
        Event event = events.get(eventId);
        if (event == null) {
            return "NOT_FOUND";
        }
        if (event.isCancelled()) {
            return "EVENT_CANCELLED";
        }

        // Case 1: Student is on the registered list
        if (event.isRegistered(userId)) {
            event.removeParticipant(userId);

            // Automated waitlist promotion in a separate thread
            if (event.getWaitlistCount() > 0) {
                Thread promotionThread = new Thread(() -> {
                    String promoted = event.pollWaitlist();
                    if (promoted != null) {
                        event.addParticipant(promoted);
                        System.out.println();
                        System.out.println("Registration cancelled. Student " + promoted
                                + " has been promoted from the waitlist to the event.");
                    }
                }, "WaitlistPromotion-Event-" + eventId);
                promotionThread.start();

                // Wait for the promotion thread to finish so output appears
                // before the next menu prompt
                try {
                    promotionThread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            return "CANCELLED_REG";
        }

        // Case 2: Student is on the waitlist
        if (event.isWaitlisted(userId)) {
            event.removeFromWaitlist(userId);
            return "CANCELLED_WAIT";
        }

        return "NOT_REGISTERED";
    }

    /**
     * Returns a list of all events (active and cancelled) that a student
     * is registered for or waitlisted on.
     *
     * @param userId the student's user ID
     * @return list of events the student is involved in
     */
    public List<Event> getEventsForStudent(String userId) {
        List<Event> result = new ArrayList<>();
        for (Event event : events.values()) {
            if (event.isRegistered(userId) || event.isWaitlisted(userId)) {
                result.add(event);
            }
        }
        return result;
    }

    // ---- Search Operations (Section 2.4) ----

    /**
     * Searches for events whose name contains the given query string.
     * The match is case-insensitive and supports partial matches.
     *
     * @param query the search term (partial or full event name)
     * @return list of matching events (may include cancelled events)
     */
    public List<Event> searchByName(String query) {
        String lowerQuery = query.toLowerCase();
        return events.values().stream()
                .filter(e -> e.getEventName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    /**
     * Searches for events that exactly match the given date string.
     *
     * @param date the date to search for in dd/mm/yyyy format
     * @return list of matching events (may include cancelled events)
     */
    public List<Event> searchByDate(String date) {
        return events.values().stream()
                .filter(e -> e.getEventDate().equals(date))
                .collect(Collectors.toList());
    }

    // ---- Persistence Helpers (Section 2.4) ----

    /**
     * Returns the internal events map. Used by the persistence layer to
     * serialise all events, registrations, and waitlists to file.
     *
     * @return unmodifiable view of the events map
     */
    public Map<Integer, Event> getEventsMap() {
        return Map.copyOf(events);
    }

    /**
     * Adds an event directly to the store. Used by the persistence layer
     * to restore saved events on application startup.
     *
     * @param event the event to add (must have a unique ID)
     */
    public void addEvent(Event event) {
        events.put(event.getEventId(), event);
    }
}
