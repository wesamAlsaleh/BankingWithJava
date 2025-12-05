package ui;

import Account.AccountService;
import Account.AccountType;
import Currency.CurrencyService;
import Global.Utils.Printer;
import User.User;

import java.util.Scanner;

public class AccountUserInterface {
    private final Scanner scanner = new Scanner(System.in);
    private final Printer printer = new Printer();
    private final AccountService accountService = new AccountService();
    private final CurrencyService currencyService = new CurrencyService();

    // todo: function to show accounts list page
    private void accountsListPage() {
    }

    // function to show create account page
    private void createAccountPage(User user) {
        // init message
        printer.printColoredTitle("Create new account");

        while (true) {
            // selected choices holder
            AccountType accountType = null;
            String currencyCode;

            // loop to select account type
            while (true) {
                // select message
                printer.printQuestion("What is the type of your new account:");
                System.out.println("[1] Saving Account");
                System.out.println("[2] Checking Account");
                printer.printPrompt("Account type: ");

                // user input
                var typeNumber = scanner.nextLine().trim();

                // make the type based on the selected choice
                switch (typeNumber) {
                    case "1":
                        accountType = AccountType.Savings;
                        break; // exit the nested while loop
                    case "2":
                        accountType = AccountType.Checking;
                        break; // exit the nested while loop
                    default:
                        printer.printWrongChoice();
                        continue; // restart the loop
                }

                break; // exit the nested while loop
            }

            // list all the currencies available in the system
            currencyService.printCurrencies();

            // loop to select currency
            while (true) {
                // select message
                printer.printQuestion("What is currency of your new account:");

                // user input
                var selectedCurrencyCode = scanner.nextLine().trim().toUpperCase();

                // if the selected currency code is more than 3 digits return error
                if (selectedCurrencyCode.length() != 3) {
                    printer.printWrongChoice();
                    continue; // restart the loop
                }

                // if the selected currency code is not in the system print error
                if (!currencyService.isCurrencyVerified(selectedCurrencyCode)) {
                    printer.printWrongChoice();
                    continue; // restart the loop
                }

                // set the code
                currencyCode = selectedCurrencyCode;

                // exit the nested loop
                break;
            }

            // create the account record
            accountService.createAccount(
                    user,
                    accountType,
                    currencyCode
            );

            // print message
            printer.printSuccessful("Account has been created successfully!");

            // exit the while loop
            break;
        }

        // it will redirect to the root page automatically
    }

    // todo: function to show delete account page
    private void deleteAccountPage() {
    }


    // function to show manage accounts page
    public void manageAccountsPage(User user) {
        // StartUp Message
        printer.printColoredTitle("Manage accounts");

        while (true) {
            // options message
            printer.printColoredLine(Printer.BLUE, "Please choose an option:");
            System.out.println("[S] See all my Accounts");
            System.out.println("[A] Create new account");
            System.out.println("[D] Delete account");
            System.out.println("[q] Quit / Logout");
            printer.printPrompt("Your choice: ");

            // input from the user
            var choice = scanner.nextLine().toLowerCase().trim();

            // based on the choice go to the next direction
            switch (choice) {
                case ("s"):
                    accountsListPage();
                    break;
                case ("a"):
                    createAccountPage(user);
                    break;
                case ("d"):
                    deleteAccountPage();
                    break;
                case ("q"):
                    printer.printSuccessful("Thank you for using GA01 Bank. Goodbye!");
                    System.exit(0); // Exit the terminal
                    break;
                default:
                    printer.printWrongChoice();
            }
        }

    }
}
