package ui;

import Account.AccountService;
import Global.Utils.Printer;
import User.User;

import java.util.Scanner;

public class AccountUserInterface {
    private final Scanner scanner = new Scanner(System.in);
    private final Printer printer = new Printer();
    private final AccountService accountService = new AccountService();

    // function to show accounts list page
    private void accountsListPage() {}

    // function to show create account page
//    private void createAccountPage() {
//        // init message
//        printer.printColoredTitle("Create new account");
//
//        while (true) {
//            // store the first name
//            System.out.println("What is your first name?");
//            var firstName = scanner.nextLine();
//
//
//            // validate the input
//            reply = null;
//
//            // if no error then create new user
//            if (reply.isEmpty()) {
//                // check if the email exists in the system
//                if (userRepository.getUserByEmail(email) != null) {
//                    // print error message
//                    printer.printError("User with email " + email + " already exists!");
//
//                    // restart the loop
//                    continue;
//                }
//
//                // generate new id
//                var id = userRepository.generateUserId();
//
//                // create user record and todo: customer record
//                var user = new User(id, firstName, lastName, email, password);
//
//                // create user file
//                userRepository.saveUser(user);
//
//                // exit the while loop
//                break;
//            } else {
//                // print the errors available
//                printer.printError(reply);
//            }
//        }
//    }

    // function to show manage accounts page
    public void manageAccountsPage() {
        // StartUp Message
        printer.printColoredTitle("Manage accounts");

        while (true) {
            // options message
            printer.printColoredLine(Printer.BLUE, "Please choose an option:");
            System.out.println("[S] See All Accounts");
            System.out.println("[A] Create new account");
            System.out.println("[D] Delete account");
            System.out.println("[q]    Quit / Logout");
            printer.printPrompt("Your choice: ");

            // input from the user
            var choice = scanner.nextLine().toLowerCase().trim();

            // based on the choice go to the next direction
            switch (choice) {
                case ("s"):
                    break;
                case ("a"):
                    break;
                case ("d"):
                    break;
                case ("q"):
                    printer.printSuccessful("Thank you for using GA01 Bank. Goodbye!");
                    break;
                default:
                    printer.printWrongChoice();
            }
        }

    }
}
