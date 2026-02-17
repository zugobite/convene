package com.zugobite.convene;

import com.zugobite.convene.controllers.MenuController;
import com.zugobite.convene.controllers.StaffMenuController;
import com.zugobite.convene.controllers.StudentMenuController;
import com.zugobite.convene.enums.Role;
import com.zugobite.convene.models.Staff;
import com.zugobite.convene.models.Student;
import com.zugobite.convene.models.User;
import com.zugobite.convene.services.EventManager;
import com.zugobite.convene.utils.ConsoleUtils;

import java.util.Scanner;

/**
 * Entry point for the Convene - Campus Event Management System.
 *
 * <p>Application flow:</p>
 * <ol>
 *   <li>Display welcome banner</li>
 *   <li>Initialise the shared {@link EventManager}</li>
 *   <li>Prompt user to select a role (Student or Staff)</li>
 *   <li>Collect user credentials (ID and name)</li>
 *   <li>Route to the appropriate role-specific menu controller</li>
 *   <li>Run the menu loop until the user exits</li>
 * </ol>
 *
 * @author Zascia Hugo
 * @version 0.2.0
 */
public class Main {

    /**
     * Application entry point. Sets up the console, initialises services,
     * performs role selection, and delegates to the appropriate menu controller.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Display welcome banner
            ConsoleUtils.printHeader("Convene - Campus Event Management System");

            // Initialise shared services
            EventManager eventManager = new EventManager();

            // Role selection and user setup
            User user = MenuController.selectRole(scanner);

            System.out.println();
            ConsoleUtils.printSuccess("Welcome, " + user.getName() + "! You are logged in as " + user.getRole() + ".");
            ConsoleUtils.printDivider();

            // Route to role-specific menu controller
            if (user.getRole() == Role.STUDENT) {
                StudentMenuController controller = new StudentMenuController((Student) user, eventManager);
                controller.run(scanner);
            } else {
                StaffMenuController controller = new StaffMenuController((Staff) user, eventManager);
                controller.run(scanner);
            }

            // Farewell message
            System.out.println();
            ConsoleUtils.printInfo("Thank you for using Convene. Goodbye, " + user.getName() + "!");
            ConsoleUtils.printDivider();

        } finally {
            scanner.close();
        }
    }
}
