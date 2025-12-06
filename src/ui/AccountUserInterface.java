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

    // function to show accounts list page
    private void accountsListPage(User user) {
        // init message
        printer.printColoredTitle("My accounts");

        // print the accounts
        accountService.printUserAccounts(user);
    }

    // function to show create account page
    private void createAccountPage(User user) {
        // init message
        printer.printColoredTitle("Create new account");

        while (true) {
            // selected choices holder
            AccountType accountType;
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
                var providedCurrencyCode = scanner.nextLine().trim().toUpperCase();

                // if the provided currency code is more than 3 digits return error
                if (providedCurrencyCode.length() != 3) {
                    printer.printWrongChoice();
                    continue; // restart the loop
                }

                // if the selected currency code is not in the system print error
                if (!currencyService.currencyExistsInTheSystem(providedCurrencyCode)) {
                    printer.printWrongChoice();
                    continue; // restart the loop
                }

                // set the code
                currencyCode = providedCurrencyCode;

                // exit the nested loop
                break;
            }

            // create the account record
            accountService.createAccount(
                    user,
                    accountType,
                    currencyCode
            );

            // exit the while loop
            break;
        }

        // it will redirect to the root page automatically
    }

    // function to show delete account page
    private void deleteAccountPage(User user) {
        // init message
        printer.printColoredTitle("Delete account");

        // print the user accounts to help choosing the account number
        accountService.printUserAccounts(user);

        while (true) {
            // if no accounts found
            if (accountService.userAccountsCount(user) == 0) {
                break; // exit the while loop
            }

            // question
            printer.printQuestion("What is the account number:");
            printer.printPrompt("Account number: ");
            var accountNumber = scanner.nextLine().trim();

            // if account number length is not 14 return error
            if (accountNumber.length() != 14) {
                printer.printError("Account number should be 14 characters!");
            }

            // delete the account
            accountService.deleteAccount(user.getId(), accountNumber);

            // exit the while loop
            break;
        }
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
                    accountsListPage(user);
                    break;
                case ("a"):
                    createAccountPage(user);
                    break;
                case ("d"):
                    deleteAccountPage(user);
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
