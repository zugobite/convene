package com.zugobite.convene.enums;

/**
 * Represents the two user roles in the Campus Event Management System.
 * Each role determines the set of actions a user is permitted to perform.
 *
 * @author Zascia Hugo
 * @version 0.1.0
 */
public enum Role {

    /** Student role - can view events, register, cancel, and check status. */
    STUDENT("Student"),

    /** Staff role - can create, update, cancel events and view participants. */
    STAFF("Staff");

    private final String displayName;

    /**
     * Constructs a Role with the given display name.
     *
     * @param displayName the human-readable name for this role
     */
    Role(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the human-readable display name of this role.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the display name when converting to string.
     *
     * @return the display name
     */
    @Override
    public String toString() {
        return displayName;
    }
}
