package com.zugobite.convene.controllers;

import com.zugobite.convene.data.DataPersistence;
import com.zugobite.convene.models.Event;
import com.zugobite.convene.models.Student;
import com.zugobite.convene.services.EventManager;
import com.zugobite.convene.utils.ConsoleUtils;
import com.zugobite.convene.utils.InputValidator;

import java.util.List;
import java.util.Scanner;

/**
 * Handles the student menu loop and dispatches actions to the appropriate
 * service methods. Each action is guarded by a permission check to enforce
 * role-based access control.
 *
 * <p>Menu options:</p>
 * <ol>
 *   <li>View Available Events</li>
 *   <li>Register for Event</li>
 *   <li>Cancel Registration</li>
 *   <li>View Registration Status</li>
 *   <li>Search Events</li>
 *   <li>Exit</li>
 * </ol>
 *
 * @author Zascia Hugo
 * @version 0.4.0
 * @see Student
 * @see EventManager
 */
public class StudentMenuController {

    /** The student user associated with this menu session. */
    private final Student student;

    /** The shared event manager for all event operations. */
    private final EventManager eventManager;

    /** The persistence layer for saving data on modification. */
    private final DataPersistence persistence;

    /**
     * Constructs a StudentMenuController for the given student.
     *
     * @param student      the authenticated student user
     * @param eventManager the shared event manager instance
     * @param persistence  the data persistence instance for auto-save
     */
    public StudentMenuController(Student student, EventManager eventManager, DataPersistence persistence) {
        this.student = student;
        this.eventManager = eventManager;
        this.persistence = persistence;
    }

    /**
     * Runs the student menu loop. Continuously displays the menu, reads
     * the user's choice, and dispatches to the corresponding action.
     * Exits when the user selects option 6.
     *
     * @param scanner the Scanner instance for reading console input
     */
    public void run(Scanner scanner) {
        boolean running = true;

        while (running) {
            student.showMenu();
            String input = scanner.nextLine().trim();

            if (!InputValidator.isValidMenuChoice(input, 6)) {
                ConsoleUtils.printError("Invalid choice. Please enter a number between 1 and 6.");
                continue;
            }

            int choice = Integer.parseInt(input);

            switch (choice) {
                case 1 -> viewEvents(scanner);
                case 2 -> registerForEvent(scanner);
                case 3 -> cancelRegistration(scanner);
                case 4 -> viewRegistrationStatus(scanner);
                case 5 -> searchEvents(scanner);
                case 6 -> running = false;
                default -> ConsoleUtils.printError("Invalid choice.");
            }

            if (running) {
                ConsoleUtils.pressEnterToContinue(scanner);
            }
        }
    }

    // ---- Section 2.2 — View Events ----

    /**
     * Displays all available (active, non-cancelled) events with sort options.
     * Shows event details including registered count and waitlist count.
     *
     * @param scanner the Scanner instance for reading input
     */
    private void viewEvents(Scanner scanner) {
        if (!student.hasPermission("VIEW_EVENTS")) {
            ConsoleUtils.printError("Access denied: Students cannot perform this action.");
            return;
        }

        ConsoleUtils.printHeader("Available Events");

        if (eventManager.getActiveEventCount() == 0) {
            ConsoleUtils.printInfo("No events are currently available.");
            return;
        }

        // Sort options
        System.out.println("Sort by:");
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
            events = eventManager.getActiveEvents();
        }

        if (events.isEmpty()) {
            ConsoleUtils.printInfo("No events to display.");
            return;
        }

        // Display each event
        ConsoleUtils.printDivider();
        for (Event event : events) {
            System.out.println(event.toDetailedString());
            ConsoleUtils.printDivider();
        }

        ConsoleUtils.printInfo("Showing " + events.size() + " active event(s).");
    }

    // ---- Section 2.3 — Registration, Waitlists, and Automation ----

    /**
     * Registers the student for an event by Event ID.
     * If the event has space, the student is registered automatically.
     * If the event is full, the student is waitlisted automatically.
     * Handles duplicate registration and cancelled event cases.
     *
     * @param scanner the Scanner instance for reading input
     */
    private void registerForEvent(Scanner scanner) {
        if (!student.hasPermission("REGISTER")) {
            ConsoleUtils.printError("Access denied: Students cannot perform this action.");
            return;
        }

        ConsoleUtils.printHeader("Register for Event");

        if (eventManager.getActiveEventCount() == 0) {
            ConsoleUtils.printInfo("No events are currently available for registration.");
            return;
        }

        // Show active events for reference
        List<Event> activeEvents = eventManager.getActiveEvents();
        ConsoleUtils.printDivider();
        for (Event event : activeEvents) {
            String status = event.hasSpace() ? "OPEN" : "FULL (waitlist)";
            System.out.println("  " + event + "  [" + status + "]");
        }
        ConsoleUtils.printDivider();
        System.out.println();

        // Prompt for event ID
        System.out.print("Enter Event ID to register for: ");
        String idInput = scanner.nextLine().trim();

        if (!InputValidator.isPositiveInteger(idInput)) {
            ConsoleUtils.printError("Invalid Event ID.");
            return;
        }

        int eventId = Integer.parseInt(idInput);
        String result = eventManager.registerStudent(eventId, student.getUserId());

        switch (result) {
            case "REGISTERED" -> {
                Event event = eventManager.getEvent(eventId);
                ConsoleUtils.printSuccess("You have been registered for \"" + event.getEventName()
                        + "\" (" + event.getRegisteredCount() + "/" + event.getMaxParticipants() + ").");
                persistence.saveData(eventManager);
            }
            case "WAITLISTED" -> {
                Event event = eventManager.getEvent(eventId);
                ConsoleUtils.printInfo("Event is full. You have been added to the waitlist for \""
                        + event.getEventName() + "\" (position " + event.getWaitlistCount() + ").");
                persistence.saveData(eventManager);
            }
            case "DUPLICATE" ->
                ConsoleUtils.printError("You are already registered or waitlisted for this event.");
            case "CANCELLED" ->
                ConsoleUtils.printError("This event has been cancelled and is not accepting registrations.");
            case "NOT_FOUND" ->
                ConsoleUtils.printError("Event with ID " + eventId + " not found.");
            default ->
                ConsoleUtils.printError("An unexpected error occurred.");
        }
    }

    /**
     * Cancels the student's registration or waitlist entry for an event.
     * If the student was registered and the waitlist is non-empty, the first
     * waitlisted student is promoted automatically in a background thread.
     *
     * @param scanner the Scanner instance for reading input
     */
    private void cancelRegistration(Scanner scanner) {
        if (!student.hasPermission("CANCEL_REGISTRATION")) {
            ConsoleUtils.printError("Access denied: Students cannot perform this action.");
            return;
        }

        ConsoleUtils.printHeader("Cancel Registration");

        // Show events the student is involved in
        List<Event> myEvents = eventManager.getEventsForStudent(student.getUserId());

        if (myEvents.isEmpty()) {
            ConsoleUtils.printInfo("You are not registered or waitlisted for any events.");
            return;
        }

        ConsoleUtils.printDivider();
        System.out.println("  Your Events:");
        ConsoleUtils.printDivider();
        for (Event event : myEvents) {
            String myStatus = event.isRegistered(student.getUserId()) ? "REGISTERED" : "WAITLISTED";
            System.out.println("  [" + event.getEventId() + "] " + event.getEventName()
                    + " — " + event.getEventDate() + " " + event.getEventTime()
                    + " [" + myStatus + "]");
        }
        ConsoleUtils.printDivider();
        System.out.println();

        // Prompt for event ID
        System.out.print("Enter Event ID to cancel registration: ");
        String idInput = scanner.nextLine().trim();

        if (!InputValidator.isPositiveInteger(idInput)) {
            ConsoleUtils.printError("Invalid Event ID.");
            return;
        }

        int eventId = Integer.parseInt(idInput);
        String result = eventManager.cancelRegistration(eventId, student.getUserId());

        switch (result) {
            case "CANCELLED_REG" -> {
                ConsoleUtils.printSuccess("Your registration for event " + eventId + " has been cancelled.");
                persistence.saveData(eventManager);
            }
            case "CANCELLED_WAIT" -> {
                ConsoleUtils.printSuccess("Your waitlist entry for event " + eventId + " has been removed.");
                persistence.saveData(eventManager);
            }
            case "NOT_REGISTERED" ->
                ConsoleUtils.printError("You are not registered or waitlisted for event " + eventId + ".");
            case "EVENT_CANCELLED" ->
                ConsoleUtils.printError("This event has already been cancelled.");
            case "NOT_FOUND" ->
                ConsoleUtils.printError("Event with ID " + eventId + " not found.");
            default ->
                ConsoleUtils.printError("An unexpected error occurred.");
        }
    }

    /**
     * Displays the student's current registration status across all events.
     * Shows whether the student is registered or waitlisted for each event.
     *
     * @param scanner the Scanner instance for reading input (reserved for future use)
     */
    private void viewRegistrationStatus(Scanner scanner) {
        if (!student.hasPermission("VIEW_STATUS")) {
            ConsoleUtils.printError("Access denied: Students cannot perform this action.");
            return;
        }

        ConsoleUtils.printHeader("Registration Status");

        List<Event> myEvents = eventManager.getEventsForStudent(student.getUserId());

        if (myEvents.isEmpty()) {
            ConsoleUtils.printInfo("You are not registered or waitlisted for any events.");
            return;
        }

        ConsoleUtils.printDivider();
        System.out.println("  Student: " + student.getName() + " (" + student.getUserId() + ")");
        ConsoleUtils.printDivider();

        int registeredCount = 0;
        int waitlistedCount = 0;

        for (Event event : myEvents) {
            String myStatus;
            if (event.isRegistered(student.getUserId())) {
                myStatus = "REGISTERED";
                registeredCount++;
            } else {
                myStatus = "WAITLISTED (position "
                        + (event.getWaitlist().indexOf(student.getUserId()) + 1) + ")";
                waitlistedCount++;
            }

            String eventStatus = event.isCancelled() ? " [EVENT CANCELLED]" : "";
            System.out.println();
            System.out.println("  Event    : [" + event.getEventId() + "] " + event.getEventName() + eventStatus);
            System.out.println("  Date     : " + event.getEventDate() + " " + event.getEventTime());
            System.out.println("  Location : " + event.getLocation());
            System.out.println("  Status   : " + myStatus);
        }

        ConsoleUtils.printDivider();
        ConsoleUtils.printInfo("Total: " + registeredCount + " registered, " + waitlistedCount + " waitlisted.");
    }

    // ---- Section 2.4 — Search Events ----

    /**
     * Searches for events by name (partial/full match) or date (exact match).
     * Displays full event details including registered count and waitlist count.
     *
     * @param scanner the Scanner instance for reading input
     */
    private void searchEvents(Scanner scanner) {
        if (!student.hasPermission("SEARCH_EVENTS")) {
            ConsoleUtils.printError("Access denied: Students cannot perform this action.");
            return;
        }

        ConsoleUtils.printHeader("Search Events");

        if (eventManager.getActiveEventCount() == 0) {
            ConsoleUtils.printInfo("No events are currently available to search.");
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
}
