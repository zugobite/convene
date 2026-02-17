package com.zugobite.convene.models;

import com.zugobite.convene.enums.Role;

import java.util.Set;

/**
 * Represents a staff user in the Campus Event Management System.
 * Staff can create, update, and cancel events, as well as view registered
 * participants and waitlists. Staff are restricted from registering for events.
 *
 * <p>Extends {@link User} with staff-specific menu and permission logic.</p>
 *
 * @author Zascia Hugo
 * @version 0.1.0
 * @see User
 * @see Student
 */
public class Staff extends User {

    /** Set of actions permitted for staff users. */
    private static final Set<String> PERMITTED_ACTIONS = Set.of(
            "CREATE_EVENT",
            "UPDATE_EVENT",
            "CANCEL_EVENT",
            "VIEW_PARTICIPANTS",
            "SEARCH_EVENTS",
            "VIEW_EVENTS"
    );

    /**
     * Constructs a new Staff member with the specified credentials.
     *
     * @param userId the unique staff ID (e.g., "ST01")
     * @param name   the staff member's display name
     */
    public Staff(String userId, String name) {
        super(userId, name, Role.STAFF);
    }

    /**
     * Displays the staff-specific menu options to the console.
     */
    @Override
    public void showMenu() {
        System.out.println();
        System.out.println("--- Staff Menu ---");
        System.out.println("1. Create Event");
        System.out.println("2. Update Event");
        System.out.println("3. Cancel Event");
        System.out.println("4. View Participants & Waitlists");
        System.out.println("5. Search Events");
        System.out.println("6. Exit");
        System.out.print("> ");
    }

    /**
     * Checks whether this staff member has permission to perform the given action.
     *
     * @param action the action string to check
     * @return {@code true} if the action is in the staff's permitted set
     */
    @Override
    public boolean hasPermission(String action) {
        return PERMITTED_ACTIONS.contains(action);
    }
}
