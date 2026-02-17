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
 *   <li>Date validation (dd/mm/yyyy)</li>
 *   <li>Time validation (HH:mm 24-hour)</li>
 *   <li>Positive integer validation</li>
 * </ul>
 *
 * @author Zascia Hugo
 * @version 0.2.0
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
     * Checks whether the given string represents a valid positive integer
     * (greater than zero).
     *
     * @param input the string to validate
     * @return {@code true} if the string is a positive integer
     */
    public static boolean isPositiveInteger(String input) {
        if (!isValidInteger(input)) {
            return false;
        }
        return Integer.parseInt(input.trim()) > 0;
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

    /**
     * Checks whether the given string is a valid date in dd/mm/yyyy format.
     * Validates structural format and logical day/month ranges.
     *
     * <p>Rules:</p>
     * <ul>
     *   <li>Must match pattern {@code dd/mm/yyyy}</li>
     *   <li>Day: 01–31 (upper-bounded per month)</li>
     *   <li>Month: 01–12</li>
     *   <li>Year: 4-digit positive integer</li>
     *   <li>Leap year handling for February</li>
     * </ul>
     *
     * @param input the string to validate
     * @return {@code true} if the string is a valid dd/mm/yyyy date
     */
    public static boolean isValidDate(String input) {
        if (!isNonEmpty(input)) {
            return false;
        }

        String trimmed = input.trim();
        String[] parts = trimmed.split("/");
        if (parts.length != 3) {
            return false;
        }

        // Each part must be numeric
        for (String part : parts) {
            if (!part.matches("\\d+")) {
                return false;
            }
        }

        int day, month, year;
        try {
            day = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            year = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            return false;
        }

        // Basic range checks
        if (year < 1 || month < 1 || month > 12 || day < 1) {
            return false;
        }

        // Days per month (non-leap year defaults)
        int[] daysInMonth = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        // Leap year adjustment
        if (isLeapYear(year)) {
            daysInMonth[2] = 29;
        }

        return day <= daysInMonth[month];
    }

    /**
     * Checks whether the given string is a valid time in HH:mm (24-hour) format.
     *
     * <p>Rules:</p>
     * <ul>
     *   <li>Must match pattern {@code HH:mm}</li>
     *   <li>Hours: 00–23</li>
     *   <li>Minutes: 00–59</li>
     * </ul>
     *
     * @param input the string to validate
     * @return {@code true} if the string is a valid HH:mm time
     */
    public static boolean isValidTime(String input) {
        if (!isNonEmpty(input)) {
            return false;
        }

        String trimmed = input.trim();
        String[] parts = trimmed.split(":");
        if (parts.length != 2) {
            return false;
        }

        // Each part must be numeric
        for (String part : parts) {
            if (!part.matches("\\d+")) {
                return false;
            }
        }

        int hours, minutes;
        try {
            hours = Integer.parseInt(parts[0]);
            minutes = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return false;
        }

        return hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59;
    }

    /**
     * Determines whether the given year is a leap year.
     *
     * @param year the year to check
     * @return {@code true} if the year is a leap year
     */
    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}
