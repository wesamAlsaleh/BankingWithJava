package ui;

import Auth.AuthenticationService;
import Global.Utils.Printer;
import User.User;
import User.UserRole;

import java.util.Scanner;

public class StartUpUserInterface {
    private final Scanner input = new Scanner(System.in);
    private final AuthenticationService authenticationService = new AuthenticationService();
    private final Printer printer = new Printer();
    private final CurrenciesUserInterface currenciesUserInterface = new CurrenciesUserInterface();

    // function to display a page title
    private void title(String title) {
        System.out.println("\n==============================");
        System.out.println("\t" + title);
        System.out.println("==============================");
    }

    // function to handle role violation
    private void isUserCustomer(String userRole) {
        if (userRole.equals(UserRole.Customer.toString())) {
            printer.printWrongChoice();
        }
    }

    // function to display the startup page
    public void startApplication() {
        // StartUp Message
        printer.printColoredTitle("Welcome to GA01 Bank");

        // wait for user to chose
        while (true) {
            // options message
            printer.printColoredLine(Printer.BLUE, "Please choose an option:");
            System.out.println("[R] Register a new account");
            System.out.println("[L] Login to your account");
            System.out.println("[Q] Quit the application");
            printer.printPrompt("Your choice: ");

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
                    printer.printSuccessful("Thank you for using GA01 Bank. Goodbye!");
                    break;
                default:
                    printer.printWrongChoice();
            }
        }
    }

    // function to display the home page
    public void homePage(User user) {
        // title
        printer.printColoredTitle("Welcome back, " + user.getFullName());

        // wait for user to chose
        while (true) {
            // options message
            printer.printColoredLine(Printer.BLUE, "Please choose an option:");
            System.out.println("[dep]  Deposit money");
            System.out.println("[with] Withdraw money");
            System.out.println("[t]    Transfer money");
            System.out.println("[acc]  My my accounts");
            System.out.println("[card] Manage my cards");

            // banker user options
            if (user.getRole().equals(UserRole.Banker)) {
                System.out.println("[curr] Manage System Currencies");
            }

            System.out.println("[q]    Quit / Logout");
            printer.printPrompt("Your choice: ");

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
                    printer.printSuccessful("Thank you for using GA01 Bank. Goodbye!");
                    break;
                case ("curr"):
                    // if user not allowed
                    isUserCustomer(user.getRole().toString());
                    // redirect to manage currencies page
                    currenciesUserInterface.manageCurrenciesPage();
                    break; //
                default:
                    printer.printWrongChoice();
            }
        }
    }
}
