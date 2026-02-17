package com.zugobite.convene.controllers;

import com.zugobite.convene.models.Student;
import com.zugobite.convene.utils.ConsoleUtils;
import com.zugobite.convene.utils.InputValidator;

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
 * @version 0.1.0
 * @see Student
 */
public class StudentMenuController {

    /** The student user associated with this menu session. */
    private final Student student;

    /**
     * Constructs a StudentMenuController for the given student.
     *
     * @param student the authenticated student user
     */
    public StudentMenuController(Student student) {
        this.student = student;
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
                case 1 -> viewEvents();
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

    /**
     * Displays all available events.
     * Stub - will be implemented in Section 2.2.
     */
    private void viewEvents() {
        if (!student.hasPermission("VIEW_EVENTS")) {
            ConsoleUtils.printError("Access denied: Students cannot perform this action.");
            return;
        }
        ConsoleUtils.printInfo("View Available Events - Coming soon (Section 2.2).");
    }

    /**
     * Registers the student for an event by Event ID.
     * Stub - will be implemented in Section 2.3.
     */
    private void registerForEvent() {
        if (!student.hasPermission("REGISTER")) {
            ConsoleUtils.printError("Access denied: Students cannot perform this action.");
            return;
        }
        ConsoleUtils.printInfo("Register for Event - Coming soon (Section 2.3).");
    }

    /**
     * Cancels the student's registration or waitlist entry.
     * Stub - will be implemented in Section 2.3.
     */
    private void cancelRegistration() {
        if (!student.hasPermission("CANCEL_REGISTRATION")) {
            ConsoleUtils.printError("Access denied: Students cannot perform this action.");
            return;
        }
        ConsoleUtils.printInfo("Cancel Registration - Coming soon (Section 2.3).");
    }

    /**
     * Displays the student's current registration status.
     * Stub - will be implemented in Section 2.3.
     */
    private void viewRegistrationStatus() {
        if (!student.hasPermission("VIEW_STATUS")) {
            ConsoleUtils.printError("Access denied: Students cannot perform this action.");
            return;
        }
        ConsoleUtils.printInfo("View Registration Status - Coming soon (Section 2.3).");
    }

    /**
     * Searches for events by name or date.
     * Stub - will be implemented in Section 2.4.
     */
    private void searchEvents() {
        if (!student.hasPermission("SEARCH_EVENTS")) {
            ConsoleUtils.printError("Access denied: Students cannot perform this action.");
            return;
        }
        ConsoleUtils.printInfo("Search Events - Coming soon (Section 2.4).");
    }
}
