package com.zugobite.convene.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Represents a campus event in the Campus Event Management System.
 * Each event has a unique ID, scheduling details, a capacity limit,
 * a registered participants list, and a waitlist queue.
 *
 * <p>Demonstrates OOP principles:</p>
 * <ul>
 *   <li><b>Encapsulation</b> — private fields with controlled access</li>
 *   <li><b>Collections</b> — {@code ArrayList} for participants, {@code LinkedList} as queue for waitlist</li>
 * </ul>
 *
 * @author Zascia Hugo
 * @version 0.2.0
 */
public class Event {

    /** Unique identifier for this event. */
    private final int eventId;

    /** Name of the event. */
    private String eventName;

    /** Date of the event in dd/mm/yyyy format. */
    private String eventDate;

    /** Time of the event in HH:mm (24-hour) format. */
    private String eventTime;

    /** Location where the event takes place. */
    private String location;

    /** Maximum number of registered participants allowed. */
    private final int maxParticipants;

    /** List of user IDs for registered participants. */
    private final List<String> registeredParticipants;

    /** Queue of user IDs for waitlisted participants (FIFO). */
    private final Queue<String> waitlist;

    /** Whether the event has been cancelled by staff. */
    private boolean cancelled;

    /**
     * Constructs a new Event with the specified details.
     *
     * @param eventId         the unique event ID (positive integer)
     * @param eventName       the name of the event
     * @param eventDate       the date in dd/mm/yyyy format
     * @param eventTime       the time in HH:mm format
     * @param location        the event location
     * @param maxParticipants the maximum number of participants
     */
    public Event(int eventId, String eventName, String eventDate,
                 String eventTime, String location, int maxParticipants) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.location = location;
        this.maxParticipants = maxParticipants;
        this.registeredParticipants = new ArrayList<>();
        this.waitlist = new LinkedList<>();
        this.cancelled = false;
    }

    // ---- Getters ----

    /**
     * Returns the unique event ID.
     *
     * @return the event ID
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * Returns the event name.
     *
     * @return the event name
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Returns the event date in dd/mm/yyyy format.
     *
     * @return the event date
     */
    public String getEventDate() {
        return eventDate;
    }

    /**
     * Returns the event time in HH:mm format.
     *
     * @return the event time
     */
    public String getEventTime() {
        return eventTime;
    }

    /**
     * Returns the event location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the maximum number of participants.
     *
     * @return the max participants
     */
    public int getMaxParticipants() {
        return maxParticipants;
    }

    /**
     * Returns an unmodifiable view of the registered participants list.
     *
     * @return list of registered user IDs
     */
    public List<String> getRegisteredParticipants() {
        return List.copyOf(registeredParticipants);
    }

    /**
     * Returns an unmodifiable view of the waitlist.
     *
     * @return list of waitlisted user IDs
     */
    public List<String> getWaitlist() {
        return List.copyOf(waitlist);
    }

    /**
     * Returns the number of currently registered participants.
     *
     * @return the registered count
     */
    public int getRegisteredCount() {
        return registeredParticipants.size();
    }

    /**
     * Returns the number of participants on the waitlist.
     *
     * @return the waitlist count
     */
    public int getWaitlistCount() {
        return waitlist.size();
    }

    /**
     * Returns whether the event has been cancelled.
     *
     * @return {@code true} if cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Checks whether there is space for more registered participants.
     *
     * @return {@code true} if the event is not at capacity
     */
    public boolean hasSpace() {
        return registeredParticipants.size() < maxParticipants;
    }

    // ---- Setters (for updatable fields) ----

    /**
     * Updates the event name.
     *
     * @param eventName the new event name
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Updates the event date.
     *
     * @param eventDate the new event date in dd/mm/yyyy format
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * Updates the event time.
     *
     * @param eventTime the new event time in HH:mm format
     */
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    /**
     * Updates the event location.
     *
     * @param location the new location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Marks the event as cancelled.
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    // ---- Registration operations ----

    /**
     * Adds a user to the registered participants list.
     * Does not check capacity — caller should verify with {@link #hasSpace()} first.
     *
     * @param userId the user ID to register
     * @return {@code true} if the user was added
     */
    public boolean addParticipant(String userId) {
        if (registeredParticipants.contains(userId) || waitlist.contains(userId)) {
            return false; // Already registered or waitlisted
        }
        return registeredParticipants.add(userId);
    }

    /**
     * Adds a user to the waitlist queue.
     *
     * @param userId the user ID to add to the waitlist
     * @return {@code true} if the user was added
     */
    public boolean addToWaitlist(String userId) {
        if (registeredParticipants.contains(userId) || waitlist.contains(userId)) {
            return false; // Already registered or waitlisted
        }
        return waitlist.offer(userId);
    }

    /**
     * Removes a user from the registered participants list.
     *
     * @param userId the user ID to remove
     * @return {@code true} if the user was removed
     */
    public boolean removeParticipant(String userId) {
        return registeredParticipants.remove(userId);
    }

    /**
     * Removes a user from the waitlist queue.
     *
     * @param userId the user ID to remove
     * @return {@code true} if the user was removed
     */
    public boolean removeFromWaitlist(String userId) {
        return waitlist.remove(userId);
    }

    /**
     * Checks if a user is registered for this event.
     *
     * @param userId the user ID to check
     * @return {@code true} if the user is registered
     */
    public boolean isRegistered(String userId) {
        return registeredParticipants.contains(userId);
    }

    /**
     * Checks if a user is on the waitlist for this event.
     *
     * @param userId the user ID to check
     * @return {@code true} if the user is waitlisted
     */
    public boolean isWaitlisted(String userId) {
        return waitlist.contains(userId);
    }

    /**
     * Removes and returns the first user from the waitlist.
     * Used for automatic promotion when a registered spot opens.
     *
     * @return the user ID at the head of the waitlist, or {@code null} if empty
     */
    public String pollWaitlist() {
        return waitlist.poll();
    }

    // ---- Display ----

    /**
     * Returns a formatted string displaying the event's full details.
     * Used for listing and search result display.
     *
     * @return multi-line formatted string with event details
     */
    public String toDetailedString() {
        String status = cancelled ? "CANCELLED" : "ACTIVE";
        return String.format(
                "  Event ID    : %d%n" +
                "  Name        : %s%n" +
                "  Date        : %s%n" +
                "  Time        : %s%n" +
                "  Location    : %s%n" +
                "  Capacity    : %d / %d%n" +
                "  Waitlisted  : %d%n" +
                "  Status      : %s",
                eventId, eventName, eventDate, eventTime, location,
                registeredParticipants.size(), maxParticipants,
                waitlist.size(), status
        );
    }

    /**
     * Returns a compact one-line summary of the event.
     *
     * @return formatted summary string
     */
    @Override
    public String toString() {
        String status = cancelled ? " [CANCELLED]" : "";
        return String.format("[%d] %s — %s %s @ %s (%d/%d registered, %d waitlisted)%s",
                eventId, eventName, eventDate, eventTime, location,
                registeredParticipants.size(), maxParticipants,
                waitlist.size(), status);
    }
}
