package com.zugobite.convene.controllers;

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
 * @version 0.2.0
 * @see Student
 * @see EventManager
 */
public class StudentMenuController {

    /** The student user associated with this menu session. */
    private final Student student;

    /** The shared event manager for all event operations. */
    private final EventManager eventManager;

    /**
     * Constructs a StudentMenuController for the given student.
     *
     * @param student      the authenticated student user
     * @param eventManager the shared event manager instance
     */
    public StudentMenuController(Student student, EventManager eventManager) {
        this.student = student;
        this.eventManager = eventManager;
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
                case 2 -> registerForEvent();
                case 3 -> cancelRegistration();
                case 4 -> viewRegistrationStatus();
                case 5 -> searchEvents();
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

    // ---- Section 2.3 Stubs ----

    /**
     * Registers the student for an event by Event ID.
     * Stub — will be implemented in Section 2.3.
     */
    private void registerForEvent() {
        if (!student.hasPermission("REGISTER")) {
            ConsoleUtils.printError("Access denied: Students cannot perform this action.");
            return;
        }
        ConsoleUtils.printInfo("Register for Event — Coming soon (Section 2.3).");
    }

    /**
     * Cancels the student's registration or waitlist entry.
     * Stub — will be implemented in Section 2.3.
     */
    private void cancelRegistration() {
        if (!student.hasPermission("CANCEL_REGISTRATION")) {
            ConsoleUtils.printError("Access denied: Students cannot perform this action.");
            return;
        }
        ConsoleUtils.printInfo("Cancel Registration — Coming soon (Section 2.3).");
    }

    /**
     * Displays the student's current registration status.
     * Stub — will be implemented in Section 2.3.
     */
    private void viewRegistrationStatus() {
        if (!student.hasPermission("VIEW_STATUS")) {
            ConsoleUtils.printError("Access denied: Students cannot perform this action.");
            return;
        }
        ConsoleUtils.printInfo("View Registration Status — Coming soon (Section 2.3).");
    }

    // ---- Section 2.4 Stub ----

    /**
     * Searches for events by name or date.
     * Stub — will be implemented in Section 2.4.
     */
    private void searchEvents() {
        if (!student.hasPermission("SEARCH_EVENTS")) {
            ConsoleUtils.printError("Access denied: Students cannot perform this action.");
            return;
        }
        ConsoleUtils.printInfo("Search Events — Coming soon (Section 2.4).");
    }
}
