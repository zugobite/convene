package com.zugobite.convene.controllers;

import com.zugobite.convene.enums.Role;
import com.zugobite.convene.models.Staff;
import com.zugobite.convene.models.Student;
import com.zugobite.convene.models.User;
import com.zugobite.convene.utils.ConsoleUtils;
import com.zugobite.convene.utils.InputValidator;

import java.util.Scanner;

/**
 * Handles the initial role selection process at application startup.
 * Prompts the user to choose between Student and Staff roles, collects
 * identification details, and returns the appropriate {@link User} subclass.
 *
 * <p>This controller enforces input validation for role selection, user ID,
 * and user name before allowing the application to proceed.</p>
 *
 * @author Zascia Hugo
 * @version 0.1.0
 * @see Student
 * @see Staff
 */
public class MenuController {

    /** Private constructor - all methods are static. */
    private MenuController() {
        // Static utility class
    }

    /**
     * Prompts the user to select a role and enter their credentials.
     * Loops until valid input is provided for each field.
     *
     * @param scanner the Scanner instance for reading console input
     * @return a {@link Student} or {@link Staff} object based on user selection
     */
    public static User selectRole(Scanner scanner) {
        Role role = promptForRole(scanner);
        String userId = promptForUserId(scanner);
        String name = promptForName(scanner);

        if (role == Role.STUDENT) {
            return new Student(userId, name);
        } else {
            return new Staff(userId, name);
        }
    }

    /**
     * Prompts the user to select a role (Student or Staff).
     * Re-prompts on invalid input.
     *
     * @param scanner the Scanner instance for reading input
     * @return the selected {@link Role}
     */
    private static Role promptForRole(Scanner scanner) {
        while (true) {
            System.out.println("Select your role:");
            System.out.println("  1. Student");
            System.out.println("  2. Staff");
            System.out.print("> ");

            String input = scanner.nextLine().trim();

            if (InputValidator.isValidMenuChoice(input, 2)) {
                int choice = Integer.parseInt(input);
                if (choice == 1) {
                    return Role.STUDENT;
                } else {
                    return Role.STAFF;
                }
            }

            ConsoleUtils.printError("Invalid choice. Please enter 1 or 2.");
            System.out.println();
        }
    }

    /**
     * Prompts the user for their user ID.
     * Re-prompts if the input is empty.
     *
     * @param scanner the Scanner instance for reading input
     * @return the validated user ID string
     */
    private static String promptForUserId(Scanner scanner) {
        while (true) {
            System.out.print("Enter your User ID: ");
            String input = scanner.nextLine().trim();

            if (InputValidator.isNonEmpty(input)) {
                return input;
            }

            ConsoleUtils.printError("User ID cannot be empty. Please try again.");
        }
    }

    /**
     * Prompts the user for their display name.
     * Re-prompts if the input is empty.
     *
     * @param scanner the Scanner instance for reading input
     * @return the validated name string
     */
    private static String promptForName(Scanner scanner) {
        while (true) {
            System.out.print("Enter your name: ");
            String input = scanner.nextLine().trim();

            if (InputValidator.isNonEmpty(input)) {
                return input;
            }

            ConsoleUtils.printError("Name cannot be empty. Please try again.");
        }
    }
}
