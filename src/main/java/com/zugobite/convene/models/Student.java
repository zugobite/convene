package com.zugobite.convene.models;

import com.zugobite.convene.enums.Role;

import java.util.Set;

/**
 * Represents a student user in the Campus Event Management System.
 * Students can view events, register for events, cancel registrations,
 * and view their registration status. Students are restricted from
 * creating, updating, or canceling events.
 *
 * <p>Extends {@link User} with student-specific menu and permission logic.</p>
 *
 * @author Zascia Hugo
 * @version 0.1.0
 * @see User
 * @see Staff
 */
public class Student extends User {

    /** Set of actions permitted for student users. */
    private static final Set<String> PERMITTED_ACTIONS = Set.of(
            "VIEW_EVENTS",
            "REGISTER",
            "CANCEL_REGISTRATION",
            "VIEW_STATUS",
            "SEARCH_EVENTS"
    );

    /**
     * Constructs a new Student with the specified credentials.
     *
     * @param userId the unique student ID (e.g., "S101")
     * @param name   the student's display name
     */
    public Student(String userId, String name) {
        super(userId, name, Role.STUDENT);
    }

    /**
     * Displays the student-specific menu options to the console.
     */
    @Override
    public void showMenu() {
        System.out.println();
        System.out.println("--- Student Menu ---");
        System.out.println("1. View Available Events");
        System.out.println("2. Register for Event");
        System.out.println("3. Cancel Registration");
        System.out.println("4. View Registration Status");
        System.out.println("5. Search Events");
        System.out.println("6. Exit");
        System.out.print("> ");
    }

    /**
     * Checks whether this student has permission to perform the given action.
     *
     * @param action the action string to check
     * @return {@code true} if the action is in the student's permitted set
     */
    @Override
    public boolean hasPermission(String action) {
        return PERMITTED_ACTIONS.contains(action);
    }
}
