package ui;

import Auth.AuthenticationService;
import Global.Utils.AnsiPrinter;
import User.User;

import java.util.Scanner;

public class UserInterface {
    private final Scanner input = new Scanner(System.in);
    private final AuthenticationService authenticationService = new AuthenticationService();
    private final AnsiPrinter ansiColors = new AnsiPrinter();

    // function to display a page title
    private void title(String title) {
        System.out.println("\n==============================");
        System.out.println("\t" + title);
        System.out.println("==============================");
    }

    // function to display the startup page
    public void startApplication() {
        // StartUp Message
        ansiColors.printColoredTitle(AnsiPrinter.CYAN, "Welcome to GA01 Bank");

        // wait for user to chose
        while (true) {
            // options message
            ansiColors.printColoredLine(AnsiPrinter.BLUE, "Please choose an option:");
            System.out.println("[R] Register a new account");
            System.out.println("[L] Login to your account");
            System.out.println("[Q] Quit the application");
            ansiColors.printPrompt("Your choice: ");

            // input from the user
            var choice = input.nextLine().toLowerCase().trim();

            // based on the choice go to the next direction
            switch (choice) {
                case ("r"):
                    authenticationService.register();
                    break;
                case ("l"):
                    authenticationService.login();
                    break;
                case ("q"):
                    ansiColors.printSuccessful("Thank you for using GA01 Bank. Goodbye!");
                    break;
                default:
                    ansiColors.printWrongChoice();
            }
        }
    }

    // function to display the home page
    public void homePage(User user) {
        // title
        ansiColors.printColoredTitle(AnsiPrinter.CYAN, "Welcome back, " + user.getFullName());

        // wait for user to chose
        while (true) {
            // options message
            ansiColors.printColoredLine(AnsiPrinter.BLUE, "Please choose an option:");
            System.out.println("[dep]  Deposit money");
            System.out.println("[with] Withdraw money");
            System.out.println("[t]    Transfer money");
            System.out.println("[acc]  My my accounts");
            System.out.println("[card] Manage my cards");
            System.out.println("[q]    Quit / Logout");
            ansiColors.printPrompt("Your choice: ");

            // input from the user
            var choice = input.nextLine().toLowerCase().trim();

            switch (choice) {
                case ("dep"):
                    break;
                case ("with"):
                    break;
                case ("t"):
                    break;
                case ("q"):
                    ansiColors.printSuccessful("Thank you for using GA01 Bank. Goodbye!");
                    break;
                default:
                    ansiColors.printWrongChoice();
            }
        }
    }
}
