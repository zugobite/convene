package com.zugobite.convene.controllers;

import com.zugobite.convene.models.Staff;
import com.zugobite.convene.utils.ConsoleUtils;
import com.zugobite.convene.utils.InputValidator;

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
 * @version 0.1.0
 * @see Staff
 */
public class StaffMenuController {

    /** The staff user associated with this menu session. */
    private final Staff staff;

    /**
     * Constructs a StaffMenuController for the given staff member.
     *
     * @param staff the authenticated staff user
     */
    public StaffMenuController(Staff staff) {
        this.staff = staff;
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
                case 1 -> createEvent();
                case 2 -> updateEvent();
                case 3 -> cancelEvent();
                case 4 -> viewParticipants();
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
     * Creates a new event.
     * Stub - will be implemented in Section 2.2.
     */
    private void createEvent() {
        if (!staff.hasPermission("CREATE_EVENT")) {
            ConsoleUtils.printError("Access denied: Staff cannot perform this action.");
            return;
        }
        ConsoleUtils.printInfo("Create Event - Coming soon (Section 2.2).");
    }

    /**
     * Updates an existing event's details.
     * Stub - will be implemented in Section 2.2.
     */
    private void updateEvent() {
        if (!staff.hasPermission("UPDATE_EVENT")) {
            ConsoleUtils.printError("Access denied: Staff cannot perform this action.");
            return;
        }
        ConsoleUtils.printInfo("Update Event - Coming soon (Section 2.2).");
    }

    /**
     * Cancels an existing event.
     * Stub - will be implemented in Section 2.2.
     */
    private void cancelEvent() {
        if (!staff.hasPermission("CANCEL_EVENT")) {
            ConsoleUtils.printError("Access denied: Staff cannot perform this action.");
            return;
        }
        ConsoleUtils.printInfo("Cancel Event - Coming soon (Section 2.2).");
    }

    /**
     * Views registered participants and waitlists for events.
     * Stub - will be implemented in Section 2.2.
     */
    private void viewParticipants() {
        if (!staff.hasPermission("VIEW_PARTICIPANTS")) {
            ConsoleUtils.printError("Access denied: Staff cannot perform this action.");
            return;
        }
        ConsoleUtils.printInfo("View Participants & Waitlists - Coming soon (Section 2.2).");
    }

    /**
     * Searches for events by name or date.
     * Stub - will be implemented in Section 2.4.
     */
    private void searchEvents() {
        if (!staff.hasPermission("SEARCH_EVENTS")) {
            ConsoleUtils.printError("Access denied: Staff cannot perform this action.");
            return;
        }
        ConsoleUtils.printInfo("Search Events - Coming soon (Section 2.4).");
    }
}
