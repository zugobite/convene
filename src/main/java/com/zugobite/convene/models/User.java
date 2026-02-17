package com.zugobite.convene.models;

import com.zugobite.convene.enums.Role;

/**
 * Abstract base class representing a user in the Campus Event Management System.
 * Provides common fields and enforces a contract for role-specific behavior
 * through abstract methods.
 *
 * <p>Demonstrates OOP principles:</p>
 * <ul>
 *   <li><b>Abstraction</b> - abstract methods force subclasses to define behavior</li>
 *   <li><b>Encapsulation</b> - private fields with public getters</li>
 *   <li><b>Inheritance</b> - extended by {@link Student} and {@link Staff}</li>
 *   <li><b>Polymorphism</b> - callers use {@code User} references for menu dispatch</li>
 * </ul>
 *
 * @author Zascia Hugo
 * @version 0.1.0
 * @see Student
 * @see Staff
 */
public abstract class User {

    /** Unique identifier for this user (e.g., "S101", "ST01"). */
    private final String userId;

    /** Display name of the user. */
    private final String name;

    /** Role assigned to this user (STUDENT or STAFF). */
    private final Role role;

    /**
     * Constructs a new User with the specified credentials.
     *
     * @param userId the unique user identifier
     * @param name   the user's display name
     * @param role   the role assigned to the user
     */
    protected User(String userId, String name, Role role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }

    /**
     * Returns the unique user ID.
     *
     * @return the user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the user's display name.
     *
     * @return the display name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the role assigned to this user.
     *
     * @return the user's role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Displays the role-specific menu options to the console.
     * Each subclass prints its own set of available actions.
     */
    public abstract void showMenu();

    /**
     * Checks whether this user has permission to perform the given action.
     *
     * @param action the action string to check (e.g., "CREATE_EVENT", "REGISTER")
     * @return {@code true} if the user is permitted, {@code false} otherwise
     */
    public abstract boolean hasPermission(String action);

    /**
     * Returns a string representation of the user.
     *
     * @return formatted string with role, ID, and name
     */
    @Override
    public String toString() {
        return role + " [" + userId + "] " + name;
    }
}
