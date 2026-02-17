package com.zugobite.convene.utils;

/**
 * Utility class providing static methods for input validation.
 * All methods are stateless and designed for reuse across the application.
 *
 * <p>Handles validation for:</p>
 * <ul>
 *   <li>Non-empty string fields</li>
 *   <li>Integer parsing and range checks</li>
 *   <li>Menu choice validation</li>
 * </ul>
 *
 * <p>Will be expanded in future sections for date, time, and capacity validation.</p>
 *
 * @author Zascia Hugo
 * @version 0.1.0
 */
public final class InputValidator {

    /** Private constructor to prevent instantiation of utility class. */
    private InputValidator() {
        // Utility class - do not instantiate
    }

    /**
     * Checks whether the given string is non-null and not empty after trimming.
     *
     * @param input the string to validate
     * @return {@code true} if the string contains non-whitespace characters
     */
    public static boolean isNonEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * Checks whether the given string can be parsed as a valid integer.
     *
     * @param input the string to validate
     * @return {@code true} if the string is a valid integer
     */
    public static boolean isValidInteger(String input) {
        if (!isNonEmpty(input)) {
            return false;
        }
        try {
            Integer.parseInt(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks whether the given string represents a valid menu choice
     * within the range [1, max].
     *
     * @param input the string to validate
     * @param max   the maximum valid menu option (inclusive)
     * @return {@code true} if the input is an integer between 1 and max
     */
    public static boolean isValidMenuChoice(String input, int max) {
        if (!isValidInteger(input)) {
            return false;
        }
        int choice = Integer.parseInt(input.trim());
        return choice >= 1 && choice <= max;
    }
}
