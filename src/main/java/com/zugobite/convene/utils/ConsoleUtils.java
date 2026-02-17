package com.zugobite.convene.utils;

import java.util.Scanner;

/**
 * Utility class providing static methods for consistent console output formatting.
 * Creates a polished, professional-looking terminal experience with banners,
 * dividers, and input prompts.
 *
 * @author Zascia Hugo
 * @version 0.1.0
 */
public final class ConsoleUtils {

    /** Width of the header banner (number of characters). */
    private static final int BANNER_WIDTH = 50;

    /** Character used for the banner border. */
    private static final char BORDER_CHAR = '=';

    /** Character used for divider lines. */
    private static final char DIVIDER_CHAR = '-';

    /** Private constructor to prevent instantiation of utility class. */
    private ConsoleUtils() {
        // Utility class - do not instantiate
    }

    /**
     * Prints a formatted header banner with the given title centered inside a box.
     *
     * <pre>
     * ╔══════════════════════════════════════════════════╗
     * ║               Your Title Here                   ║
     * ╚══════════════════════════════════════════════════╝
     * </pre>
     *
     * @param title the title text to display inside the banner
     */
    public static void printHeader(String title) {
        String top = "+" + repeat(BORDER_CHAR, BANNER_WIDTH) + "+";
        String bottom = "+" + repeat(BORDER_CHAR, BANNER_WIDTH) + "+";

        // Centre the title within the banner width
        int padding = BANNER_WIDTH - title.length();
        int leftPad = padding / 2;
        int rightPad = padding - leftPad;

        String middle = "|" + repeat(' ', leftPad) + title + repeat(' ', rightPad) + "|";

        System.out.println();
        System.out.println(top);
        System.out.println(middle);
        System.out.println(bottom);
        System.out.println();
    }

    /**
     * Prints a horizontal divider line across the console.
     */
    public static void printDivider() {
        System.out.println(repeat(DIVIDER_CHAR, BANNER_WIDTH + 2));
    }

    /**
     * Prompts the user to press Enter to continue.
     * Useful for pausing after displaying results.
     *
     * @param scanner the Scanner instance for reading input
     */
    public static void pressEnterToContinue(Scanner scanner) {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Prints an error message with a consistent prefix.
     *
     * @param message the error message to display
     */
    public static void printError(String message) {
        System.out.println("[ERROR] " + message);
    }

    /**
     * Prints a success message with a consistent prefix.
     *
     * @param message the success message to display
     */
    public static void printSuccess(String message) {
        System.out.println("[OK] " + message);
    }

    /**
     * Prints an informational message with a consistent prefix.
     *
     * @param message the informational message to display
     */
    public static void printInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    /**
     * Repeats a character the specified number of times.
     *
     * @param ch    the character to repeat
     * @param count the number of repetitions
     * @return a string consisting of the character repeated count times
     */
    private static String repeat(char ch, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }
}
