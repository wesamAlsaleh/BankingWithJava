package ui;

import Account.Account;
import Account.AccountService;
import Account.AccountType;
import Auth.AuthenticationService;
import Card.DebitCardService;
import Currency.CurrencyService;
import Global.Utils.Printer;
import Transaction.DateFilter;
import Transaction.TransactionService;
import Transaction.TransactionType;
import User.User;
import User.UserRole;
import User.UserRepository;
import User.UserValidation;


import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserInterfaces {
    private final Scanner scanner = new Scanner(System.in);
    private final Printer printer = new Printer();
    private final AuthenticationService authenticationService = new AuthenticationService();
    private final AccountService accountService = new AccountService();
    private final CurrencyService currencyService = new CurrencyService();
    private final TransactionService transactionService = new TransactionService();
    private final UserRepository userRepository = new UserRepository();
    private final UserValidation userValidation = new UserValidation();
    private final DebitCardService debitCardService = new DebitCardService();

    // function to validate the country
    private String validateCountry(String country) {
        // if the name is empty
        if (country.isBlank()) {
            return "Country is required!";
        }

        // if the country is less than 4 characters return error
        if (country.length() < 4) {
            return "Country must be at least 4 characters!";
        }

        return "";
    }

    // function to validate currency code input
    private String validateCurrencyCode(String currencyCode) {
        // validate the code
        if (currencyCode == null || currencyCode.length() != 3)
            return "Please enter a valid currency code!";

        // return empty string
        return "";
    }

    // function to validate currency exchange rate input
    private String validateExchangeRate(double exchangeRate) {
        // validate the code
        if (exchangeRate <= 0) return "Please enter a valid exchange rate!";

        // return empty string
        return "";
    }

    // function to validate the input for create new currency record
    private String validateCreateCurrencyInput(String country, String currencyCode, double exchangeRate) {
        // messages holder
        StringBuilder stringBuilder = new StringBuilder();

        // if there are any safety violation
        stringBuilder.append(validateCountry(country));
        stringBuilder.append(validateCurrencyCode(currencyCode));
        stringBuilder.append(validateExchangeRate(exchangeRate));

        // return the messages
        return stringBuilder.toString();
    }

    // function to validate the input for deleting currency record
    private String validateDeleteCurrencyInput(String currencyCode) {
        return validateCurrencyCode(currencyCode);
    }

    // function to handle role violation
    private void userMiddleware(String userRole) {
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
            var choice = scanner.nextLine().toLowerCase().trim();

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
            System.out.println("[S]    See all my Accounts");
            System.out.println("[C]    Create new account");
            System.out.println("[D]    Delete account");
            System.out.println("[DEP]  Deposit money");
            System.out.println("[WITH] Withdraw money");
            System.out.println("[T]    Transfer money");
            System.out.println("[TH]   Transfer history");
            System.out.println("[PB]   Pay by debit card");

            // options requires to pass the middleware
            if (user.getRole().equals(UserRole.Banker)) {
                printer.printColoredLine(Printer.PURPLE, "[SC]   See All Currencies");
                printer.printColoredLine(Printer.PURPLE, "[AC]   Add currency");
                printer.printColoredLine(Printer.PURPLE, "[DC]   Delete currency");
                printer.printColoredLine(Printer.PURPLE, "[ST]   System transactions");

            }
            printer.printColoredLine(Printer.RED, "[Q]    Quit / Logout");
            printer.printPrompt("Your choice: ");

            // input from the user
            var choice = scanner.nextLine().toLowerCase().trim();

            switch (choice) {
                case ("s"):
                    accountsListPage(user);
                    break;
                case ("c"):
                    createAccountPage(user);
                    break;
                case ("d"):
                    deleteAccountPage(user);
                    break;
                case ("dep"):
                    depositPage(user);
                    break;
                case ("with"):
                    withdrawPage(user);
                    break;
                case ("t"):
                    transferToPage(user);
                    break;
                case ("th"):
                    userTransferHistoryPage(user);
                    break;
                case "pb":
                    payByDebitCardPage(user);
                    break;
                case ("sc"):
                    // if user not allowed this option is not available
                    userMiddleware(user.getRole().toString());
                    seeAllCurrenciesPage();
                    break;
                case ("ac"):
                    // if user not allowed this option is not available
                    userMiddleware(user.getRole().toString());
                    addCurrencyPage();
                    break;
                case ("dc"):
                    // if user not allowed this option is not available
                    userMiddleware(user.getRole().toString());
                    deleteCurrenciesPage();
                    break;
                case ("st"):
                    // if user not allowed this option is not available
                    userMiddleware(user.getRole().toString());
                    seeSystemTransactions();
                    break;
                case ("q"):
                    printer.printSuccessful("Thank you for using GA01 Bank. Goodbye!");
                    System.exit(0); // Exit
                    break;
                default:
                    printer.printWrongChoice();
            }
        }
    }

    // *************************************************************************
    // -------------------------------Account Pages----------------------------------
    // *************************************************************************

    // function to show create account page
    private void createAccountPage(User user) {
        // init message
        printer.printColoredTitle("Create new account");

        while (true) {
            // selected choices holder
            AccountType accountType;
            String currencyCode;
            String accountName;

            // loop to select account type
            while (true) {
                // select message
                printer.printQuestion("What is the type of your new account:");
                System.out.println("[1] Saving Account");
                System.out.println("[2] Checking Account");
                printer.printPrompt("Account type: ");

                // user input
                var typeNumber = scanner.nextLine().trim();

                // set the type based on the selected choice
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
                // currency input
                printer.printQuestion("What is currency of your new account:");
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

            // loop to select account name
            while (true) {
                // account name input
                printer.printQuestion("What is the account name?");
                var name = scanner.nextLine().trim();

                // if the name length is too short return error and repeat the question
                if (name.length() < 3) {
                    printer.printError("The account name is too short!");
                    continue; // restart the loop
                }

                // set the name
                accountName = name;

                // exit the nested loop
                break;
            }

            // create the account record
            var success = accountService.createAccount(
                    user,
                    accountType,
                    currencyCode,
                    accountName
            );

            // if the operation failed reset the loop
            if (!success) continue; // restart the while loop

            // exit the while loop
            break;
        }
        // it will redirect to the root page automatically
    }

    // function to show accounts list page
    private void accountsListPage(User user) {
        // init message
        printer.printColoredTitle("My accounts");

        // print the accounts
        accountService.printUserAccounts(user);
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
                continue; // restart the loop
            }

            // delete the account
            accountService.deleteAccount(user.getId(), accountNumber);

            // exit the while loop
            break;
        }
    }

    // *************************************************************************
    // -------------------------------Currencies Pages----------------------------------
    // *************************************************************************

    // function to show the add currencies page
    private void addCurrencyPage() {
        // init message
        printer.printColoredTitle("Add new currency");

        while (true) {
            try {
                // country name input
                printer.printQuestion("What is the country:");
                String country = scanner.nextLine();

                // currency code input
                printer.printQuestion("What is the currency code:");
                String currencyCode = scanner.nextLine();

                // exchange rate input as double!
                printer.printQuestion("What is the exchange rate of: ");
                printer.printColored(Printer.YELLOW, "The currency rate must relative to USD: ");
                double exchangeRate = scanner.nextDouble();
                scanner.nextLine(); // consume newline after nextDouble() !

                // validate the input
                var reply = validateCreateCurrencyInput(country, currencyCode, exchangeRate);

                // if no error proceed the operation
                if (reply.isEmpty()) {
                    // add the currency to the system
                    var success = currencyService.addCurrency(country, currencyCode, exchangeRate);

                    // if the operation failed reset the loop
                    if (!success) continue; // restart the loop

                    // exit the while loop
                    break;
                }
            } catch (InputMismatchException e) {
                printer.printError("Please enter a valid code and exchange rate!");
                scanner.nextLine(); // flush the buffer
            }
        }
    }

    // function to show see all currencies page
    private void seeAllCurrenciesPage() {
        // init message
        printer.printColoredTitle("See all currencies");

        // print the all the currencies in the system
        currencyService.printCurrencies();
    }

    // function to show delete currencies page
    public void deleteCurrenciesPage() {
        // init message
        printer.printColoredTitle("Delete currencies");

        // print the all the currencies in the system to help the user
        currencyService.printCurrencies();

        while (true) {
            // if no currencies found
            if (currencyService.getCurrencies().isEmpty()) {
                break; // exit the while loop
            }

            printer.printQuestion("What is the currency code:");
            String currencyCode = scanner.nextLine();

            // validate the input
            var reply = validateDeleteCurrencyInput(currencyCode);

            // if no errors
            if (reply.isEmpty()) {
                // delete the currency from the system
                currencyService.deleteCurrency(currencyCode);
            }

            // exit the while loop to return the parent page
            break;
        }
        // automatically redirected
    }

    // *************************************************************************
    // -------------------------------Transaction Pages----------------------------------
    // *************************************************************************

    // function to show the system transactions
    public void seeSystemTransactions() {
        // init message
        printer.printColoredTitle("System transactions (Ascending)");

        // print the transactions
        transactionService.printSystemTransactions();
    }

    // function to show the transfer history for the user page
    private void userTransferHistoryPage(User user) {
        // init message
        printer.printColoredTitle("Transactions History");

        // values holder
        DateFilter dateFilter;
        String operation;

        // date input
        while (true) {
            printer.printQuestion("Get transactions history after:");
            System.out.println("[TT] Today's transactions");
            System.out.println("[YT] Yesterday's transactions]");
            System.out.println("[WT] Last week transactions");
            System.out.println("[MT] Last month transactions");

            // user input
            var filter = scanner.nextLine().trim().toLowerCase();

            // select the result based on the input
            switch (filter) {
                case "tt":
                    dateFilter = DateFilter.TODAY;
                    break;
                case "yt":
                    dateFilter = DateFilter.YESTERDAY;
                    break;
                case "wt":
                    dateFilter = DateFilter.LAST_WEEK;
                    break;
                case "mt":
                    dateFilter = DateFilter.LAST_MONTH;
                    break;
                default:
                    printer.printWrongChoice();
                    continue; // restart the loop
            }

            break; // exit the while loops
        }

        // operation type input
        while (true) {
            printer.printQuestion("Type of transaction:");
            System.out.println("[1] Deposits");
            System.out.println("[2] Withdrawals");
            System.out.println("[3] Transfers");
            System.out.println("[4] All transactions");

            // user input
            operation = scanner.nextLine().toLowerCase().trim();

            // select based on the user
            switch (operation) {
                case "1":
                    operation = TransactionType.DEPOSIT.toString();
                    break;
                case "2":
                    operation = TransactionType.WITHDRAW.toString();
                    break;
                case "3":
                    operation = TransactionType.TRANSFER.toString();
                    break;
                case "4":
                    operation = "ALL";
                    break;
                default:
                    printer.printWrongChoice();
            }

            // exit the while loop
            break;
        }

        // print the transactions based on the filter and operation type
        transactionService.printUserTransactions(user, dateFilter, operation);
    }

    // function to show the transfer page questions
    private void showReceiverQuestions(User receiver, String receiverAccountNumber, List<Account> receiverAccounts, Account receiverAccount, Account senderAccount, double amount) {

    }

    // function to show transfer to page
    private void transferToPage(User sender) {
        // init message
        printer.printColoredTitle("Transfer Account");

        // values holder
        User receiver = null;
        String receiverAccountNumber = "";
        List<Account> receiverAccounts = List.of();
        Account receiverAccount = null; // null for type safety
        Account senderAccount = null; // null for type safety
        double amount = 0;

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

    // function to show deposit page
    private void depositPage(User user) {
        // init message
        printer.printColoredTitle("Deposit to account");

        // holders
        Account account = null;
        double amount;

        while (true) {
            // get the user accounts
            var userAccounts = accountService.getUserAccounts(user);

            // if the user has no accounts show message
            if (userAccounts.isEmpty()) {
                printer.printError("No user accounts found!");
                break; // exit the while loop
            }

            // print the accounts to help selecting
            accountService.printUserAccounts(user);

            // account selector
            while (true) {
                // question
                printer.printQuestion("Select the account to deposit:");
                printer.printPrompt("Account number to deposit: ");

                // user input
                var accountNumber = scanner.nextLine().trim();

                // flag to check if the account number belon to the user
                var flag = false;

                // if the account number do not belong to the user send error
                for (Account userAccount : userAccounts) {
                    // if it belongs to the user
                    if (userAccount.getAccountNumber().equals(accountNumber)) {
                        flag = true; // set the flag to true
                        account = userAccount; // set the account
                        break; // no need to check the others
                    }
                }

                // if the account number does not belong to the user send error
                if (!flag) {
                    printer.printError("Invalid account number!");
                    continue; // restart the loop
                }

                // exit the nested while loop
                break;
            }

            // amount input
            while (true) {
                printer.printQuestion("Enter amount to transfer:");

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

            // perform the deposit
            accountService.deposit(account, amount);

            // exit the while loop after the operation
            break;
        }
    }

    // function to show withdraw page
    private void withdrawPage(User user) {
        // init message
        printer.printColoredTitle("Withdraw from account");

        // holders
        Account account = null;
        double amount;

        while (true) {
            // get the user accounts
            var userAccounts = accountService.getUserAccounts(user);

            // if the user has no accounts show message
            if (userAccounts.isEmpty()) {
                printer.printError("No user accounts found!");
                break; // exit the while loop
            }

            // print the accounts to help selecting
            accountService.printUserAccounts(user);

            // account selector
            while (true) {
                // question
                printer.printQuestion("Select the account to withdraw:");
                printer.printPrompt("Account number to withdraw: ");

                // user input
                var accountNumber = scanner.nextLine().trim();

                // flag to check if the account number belong to the user
                var flag = false;

                // if the account number do not belong to the user send error
                for (Account userAccount : userAccounts) {
                    // if it belongs to the user
                    if (userAccount.getAccountNumber().equals(accountNumber)) {
                        flag = true; // set the flag to true
                        account = userAccount; // set the account
                        break; // no need to check the others
                    }
                }

                // if the account number does not belong to the user send error
                if (!flag) {
                    printer.printError("Invalid account number!");
                    continue; // restart the loop
                }

                // exit the nested while loop
                break;
            }

            // amount input
            while (true) {
                printer.printQuestion("Enter amount to transfer:");

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

            // perform the deposit
            accountService.withdraw(account, amount);

            // exit the while loop after the operation
            break;
        }
    }

    // *************************************************************************
    // -------------------------------Debit Card Pages----------------------------------
    // *************************************************************************

    // function to show debit card page
    public void payByDebitCardPage(User user) {
        // init message
        printer.printColoredTitle("Transaction using debit card");

        // values holder
        String cardNumber;
        String operation;

        // transfer values
        User receiver;
        String receiverAccountNumber = "";
        List<Account> receiverAccounts;
        Account receiverAccount = null; // null for type safety
        Account senderAccount = null; // null for type safety
        double amount;

        while (true) {
            // select card to use
            while (true) {
                // print the debit cards for the user
                debitCardService.printUserCards(user.getId());

                // question
                printer.printQuestion("Chose what debit card to use:");
                printer.printPrompt("Card number: ");

                // user input
                cardNumber = scanner.nextLine().trim();

                // validate the number
                var reply = debitCardService.validateCardNumber(cardNumber);

                // if errors retry
                if (!reply.isEmpty()) {
                    printer.printError(reply);
                    continue; // restart the loop
                }

                // exit nested while loop
                break;
            }

            // select the operation to do
            while (true) {
                // question
                printer.printQuestion("What operation would you like to do: ");
                System.out.println("[1] Deposit");
                System.out.println("[2] Withdraw");
                System.out.println("[3] Transfer");
                printer.printPrompt("Operation: ");

                // user input
                operation = scanner.nextLine().trim();

                // set the operation based on the input
                switch (operation) {
                    case "1":
                        operation = TransactionType.DEPOSIT.toString();
                        break;
                    case "2":
                        operation = TransactionType.WITHDRAW.toString();
                        break;
                    case "3":
                        operation = TransactionType.TRANSFER.toString();
                        break;
                    default:
                        printer.printWrongChoice();
                        continue; // restart the question
                }

                // exit the nested while loop
                break;
            }

            // show the next question based on the operation
            if (operation.equals(TransactionType.TRANSFER.toString())) {
                // get the account linked to the card
                var debitCard = debitCardService.getDebitCardByCardNumber(cardNumber);

                // if the debit card in not found
                if (debitCard == null) {
                    printer.printError("Debit card not found!");
                    continue; // restart the loop, should not happen in the first place!
                }

                // get the account linked to the debit card
                var linkedAccount = accountService.getAccountByAccountNumber(debitCard.getAccountNumber());

                // if the account in not found
                if (linkedAccount == null) {
                    printer.printError("Account not found!");
                    continue; // restart the loop, should not happen in the first place!
                }

                // set the sender account
                senderAccount = linkedAccount;

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
            } else {
                // select the amount
                while (true) {
                    printer.printQuestion("Enter amount: ");

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
            }

            // do the operation based on the selected value
            switch (operation) {
                case "DEPOSIT":
                    debitCardService.depositMoney(cardNumber, amount);
                    break;
                case "WITHDRAW":
                    debitCardService.withdrawMoney(cardNumber, amount);
                    break;
                case "TRANSFER":
                    debitCardService.transferMoney(cardNumber, receiverAccountNumber, amount);
                    break;
                default:
                    printer.printWarning("Invalid operation!");
                    continue;
            }

            // exit the while to go to the home page
            break;
        }
    }

}
