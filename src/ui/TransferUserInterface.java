package ui;

import Account.Account;
import Account.AccountRepository;
import Account.AccountService;
import Global.Utils.Printer;
import User.User;
import User.UserRepository;
import User.UserValidation;

import java.util.List;
import java.util.Scanner;

public class TransferUserInterface {
    private final Scanner scanner = new Scanner(System.in);
    private final Printer printer = new Printer();
    private final AccountService accountService = new AccountService();
    private final UserRepository userRepository = new UserRepository();
    private final UserValidation userValidation = new UserValidation();

    // function to show the transfer history page
    private void transferHistoryPage() {
    }

    // function to show transfer to page
    private void transferToPage(User sender) {
        // init message
        printer.printColoredTitle("Transfer Account");

        // values holder
        User receiver;
        String receiverAccountNumber;
        List<Account> receiverAccounts;
        Account receiverAccount = null; // null for type safety
        Account senderAccount = null; // null for type safety
        double amount;

        while (true) {
            // sender account number input
            while (true) {
                // print the sender accounts
                accountService.printUserAccounts(sender);

                // question
                printer.printQuestion("Choose your account:");
                printer.printPrompt("My account number: ");

                // user input
                var accountNumber = scanner.nextLine().trim();

                // validate the account number
                var reply = accountService.validateAccountNumber(accountNumber);

                // if there are errors show message
                if (!reply.isEmpty()) {
                    printer.printError("Invalid account number!");
                    continue; // restart the loop
                }

                // get the sender accounts for validation
                var senderAccounts = accountService.getUserAccounts(sender);

                // if no accounts show message
                if (senderAccounts.isEmpty()) {
                    printer.printError("No user accounts found!");
                    continue; // restart the loop
                }

                // flag to check if the account belong to the user
                var accountBelongToTheUser = false;

                // iterate over the accounts
                for (Account account : senderAccounts) {

                    if (account.getAccountNumber().equals(accountNumber)) {
                        accountBelongToTheUser = true; // set to true
                        senderAccount = account; // set the sender account holder
                        break; // no need to check the others
                    }
                }

                //  if the account number does not belong to the sender user send error
                if (!accountBelongToTheUser) {
                    printer.printError("Invalid account number!");
                    continue; // restart the loop
                }

                // exit the nested while
                break;
            }

            // email input
            while (true) {
                printer.printQuestion("Enter user email:");
                printer.printPrompt("Email to transfer: ");

                // input from the user
                var email = scanner.nextLine().trim();

                // validate the email
                var reply = userValidation.validateEmailInput(email);

                // if there is error
                if (!reply.isEmpty()) {
                    // show error message
                    printer.printError("Invalid email address!");
                    continue; // restart the while loop
                }

                // get the user by his email
                receiver = userRepository.getUserByEmail(email);

                // if the user in not available return error
                if (receiver == null) {
                    printer.printError("User with email " + email + " not found!");
                    continue; // restart the while loop
                }

                // get the user accounts
                receiverAccounts = accountService.getUserAccounts(receiver);

                // if no user account print message
                if (receiverAccounts.isEmpty()) {
                    printer.printError("This user does not have any account!");
                    continue; // restart
                }

                // print them
                accountService.printAccountsToTransfer(receiverAccounts);

                // exit the nested while loop
                break;
            }

            // receiver account number input
            while (true) {
                printer.printQuestion("Enter account number:");
                printer.printPrompt("Account number to transfer: ");

                // user input
                receiverAccountNumber = scanner.nextLine().trim();

                // validate the account number
                var accNumError = accountService.validateAccountNumber(receiverAccountNumber);

                // if there is errors
                if (!accNumError.isEmpty()) {
                    printer.printError("Invalid account number!");
                    continue; // restart the while loop
                }

                // flag to check if the account belong to the user
                var accountBelongToTheUser = false;

                // iterate the user accounts
                for (Account account : receiverAccounts) {
                    // if the account number belong to the user set the flag
                    if (account.getAccountNumber().equals(receiverAccountNumber)) {
                        accountBelongToTheUser = true; // set belong to flag
                        receiverAccount = account; // set the account
                        break; // no need to check the others
                    }
                }

                // if the account number provided does not belong to the user return error
                if (!accountBelongToTheUser) {
                    printer.printError("Wrong account number!");
                    continue; // restart the loop
                }

                // exit the nested while loop
                break;
            }

            // amount to pay input
            while (true) {
                printer.printQuestion("Enter amount to transfer:");
                printer.printPrompt(String.format("Amount to transfer (%s -> %s): ",
                        senderAccount.getCurrency(),
                        receiverAccount.getCurrency())
                );

                // user input
                amount = Double.parseDouble(scanner.nextLine().trim());

                // validate the number
                if (amount <= 0) {
                    printer.printError("Invalid amount!");
                    continue; // restart the loop
                }

                // exit the nested while loop
                break;
            }

            // transfer money from sender to receiver
            accountService.transfer(
                    receiver,
                    receiverAccountNumber,
                    senderAccount,
                    amount
            );

            // exit the loop
            break;
        }
    }

    // function to show the transfer page
    public void transferPage(User user) {
        // init message
        printer.printColoredTitle("Transfer");
        System.out.println("[H] Transfer history");
        System.out.println("[T] Transfer to");
        System.out.println("[q] Quit / Logout");
        printer.printPrompt("Your choice: ");

        // input from the user
        var choice = scanner.nextLine().toLowerCase().trim();

        // based on the choice go to the next direction
        switch (choice) {
            case ("h"):
                break;
            case ("t"):
                transferToPage(user);
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
