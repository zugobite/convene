package com.zugobite.convene.services;

import com.zugobite.convene.models.Event;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class managing all event operations in the Campus Event Management System.
 * Acts as the central repository for events and provides business logic for
 * creating, updating, cancelling, listing, and sorting events.
 *
 * <p>Demonstrates:</p>
 * <ul>
 *   <li><b>Collections</b> — {@code HashMap} for O(1) event lookup by ID</li>
 *   <li><b>Modular design</b> — reusable methods for all event operations</li>
 *   <li><b>Encapsulation</b> — events stored privately, accessed through methods</li>
 * </ul>
 *
 * @author Zascia Hugo
 * @version 0.2.0
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
}
