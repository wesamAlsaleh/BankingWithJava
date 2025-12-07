package Account;

import Currency.Currency;
import Currency.CurrencyRepository;
import Global.Utils.Printer;
import Transaction.TransactionService;
import Transaction.TransactionType;
import User.User;
import User.UserRepository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AccountService {
    private final AccountRepository accountRepository = new AccountRepository();
    private final CurrencyRepository currencyRepository = new CurrencyRepository();
    private final TransactionService transactionService = new TransactionService();
    private final UserRepository userRepository = new UserRepository();
    private final Printer printer = new Printer();

    private static final String COUNTRY_CODE = "BH";
    private static final String BANK_CODE = "5354"; // fake bank code
    private static final String BANK_IDENTIFIER = "GAJB"; // fake bank identifier "General Assembly Java Bootcamp"

    // function to generate check digits for the iban generator
    private String generateCheckDigits(String accountNumber) throws IllegalArgumentException {
        // if the account number is less than 12 return error
        if (accountNumber.length() != 14) {
            throw new IllegalArgumentException("Account number must be 14 digits.");
        }

        // (5354****BH00 => 5354****111700) where B = 11 and H = 17 and two placeholders
        String wholeNumber = accountNumber + "111700";

        // convert the whole number to Big integer to perform the reminder
        var number = new BigInteger(wholeNumber);

        // calculate the modulo 97
        var modulo97 = number.remainder(BigInteger.valueOf(97));

        // calculate the check digit, check digit = 98 - modulo97 (as integer)
        var checkDigit = 98 - modulo97.intValue();

        // return the checksum (leading 0 if less than 2 digits)
        return String.format("%02d", checkDigit);
    }

    // function to structure the iban on a specific formation
    private String structureTheIban(String checkDigits, String accountNumber, String separator) {
        // string instance
        StringBuilder stringBuilder = new StringBuilder();

        // format the account number (14 digits) as **** **** **** (0 -> 3, 4 -> 7, 8 -> 11, 12 -> 13)
        for (int i = 0; i < accountNumber.length(); i++) {
            // add the separator after the following indexes (3, 7, 11, 13)
            switch (i) {
                case 3, 7, 11:
                    stringBuilder.append(accountNumber.charAt(i));
                    stringBuilder.append(separator); // add separator
                    continue; // go to next index
                default:
                    stringBuilder.append(accountNumber.charAt(i));
            }
        }

        return String.format("%s%s%s%s%s%s", COUNTRY_CODE, checkDigits, separator, BANK_IDENTIFIER, separator, stringBuilder);
    }

    // function to generate account number
    private String generateAccountNumber() {
        // string container
        StringBuilder digitsHolder = new StringBuilder();

        // account number container
        String accountNumber;

        // if the generated number exist re-generate
        do {
            // generate 10 random digits
            for (int i = 0; i < 10; i++) {
                digitsHolder.append((int) (Math.random() * 10)); // Random number from 0 to 9
            }

            // concatenate the bank code with the random number
            accountNumber = BANK_CODE + digitsHolder;
        } while (accountRepository.existInAccountsListFile(accountNumber));

        // save it in the db "account_number_list.txt"
        accountRepository.saveAccountsListFile(accountNumber);

        // return the number
        return accountNumber;
    }

    // function to generate iban number
    private String generateIban(String accountNumber) {
        // ***** Header *****
        // country code (2 Alpha characters)
        // constant

        // check digit (2 Numeric characters)
        var checkDigits = generateCheckDigits(accountNumber);

        // ***** Basic bank account number *****
        // bank identifier (4 Alpha characters)
        // constant

        // account number (10 Numeric or Alphanumeric characters)
        // provided

        return structureTheIban(checkDigits, accountNumber, " ");
    }

    // function to generate deposit message (deposit/withdraw only!)
    private void successOperationMessage(TransactionType transferType, Account account, double amount) {
        // set the operation type based on the input
        switch (transferType) {
            case DEPOSIT:
                printer.printColoredLine(Printer.YELLOW, String.format("Success! deposit+ of %s %.2f to %s REF IBAN *****%s is completed on %s Balance %s %s",
                        account.getCurrency(),
                        amount,
                        account.getAccountName(),
                        account.getIban().substring(21).replaceAll("\\s+", ""), // last 5 digits in the Iban (\\s removes white spaces, while the + mean one or more)
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        account.getCurrency(),
                        account.getBalance()
                ));
                break;
            case WITHDRAW:
                printer.printColoredLine(Printer.YELLOW, String.format("Success! withdraw- of %s %.2f from %s REF IBAN *****%s is completed on %s Balance %s %s",
                        account.getCurrency(),
                        amount,
                        account.getAccountName(),
                        account.getIban().replaceAll("\\s+", "").substring(17), // last 5 digits in the Iban (\\s removes white spaces, while the + mean one or more)
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        account.getCurrency(),
                        account.getBalance()
                ));
                break;
        }
    }

    // function to generate transfer message
    private void successTransferMessage(Account senderAccount, Account receiverAccount, double amount) {
        printer.printColoredLine(Printer.YELLOW, String.format("Success! transfer++ of %s %.2f to %s REF IBAN *****%s is completed on %s Balance %s %s",
                senderAccount.getCurrency(),
                amount,
                receiverAccount.getAccountName(),
                receiverAccount.getIban().substring(21).replaceAll("\\s+", ""), // last 5 digits in the Iban (\\s removes white spaces, while the + mean one or more)
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                senderAccount.getCurrency(),
                senderAccount.getBalance()
        ));
    }

    // function to create account record
    public boolean createAccount(User user, AccountType accountType, String currency, String accountName) {
        // if the currency is not in the system return false
        if (!currencyRepository.isCurrencyAvailable(currency)) return false;

        // generate a unique account number
        var accountNumber = generateAccountNumber();

        // generate iban
        var iban = generateIban(accountNumber);

        // create new account object
        var account = new Account(
                user.getId(),
                accountNumber,
                iban,
                accountName,
                accountType,
                currency
        );

        // prepare the file name as accountNumber-userId.txt
        var fileName = accountNumber + "-" + user.getId() + ".txt";

        // get the user accounts
        List<Account> userAccounts = accountRepository.getAllAccountsByUserId(user.getId());

        // iterate over the accounts
        for (Account userAccount : userAccounts) {
            //  if the user has an account with the same type and currency code
            if (userAccount.getCurrency().equals(currency) && userAccount.getAccountType().equals(accountType)) {
                // send message
                printer.printError("Account with the same currency already exists.");

                // return false
                return false;
            }
        }

        // create new record
        return accountRepository.saveNewAccountRecord(fileName, account.createAccountRecord());
    }

    // function to delete account
    public void deleteAccount(Long userId, String accountNumber) {
        accountRepository.deleteAccountRecord(userId, accountNumber);
    }

    // function to get user accounts
    public List<Account> getUserAccounts(User user) {
        // get all the accounts of the user
        return accountRepository.getAllAccountsByUserId(user.getId());
    }

    // function to get the number of the accounts belonged to the user
    public Integer userAccountsCount(User user) {
        // get the accounts
        var accounts = accountRepository.getAllAccountsByUserId(user.getId());

        // return the length of them
        return accounts.size();
    }

    // function to format the print of the accounts
    public void printUserAccounts(User user) {
        // get the user accounts
        var userAccounts = getUserAccounts(user);

        // if no accounts
        if (userAccounts.isEmpty()) {
            printer.printColoredLine(Printer.YELLOW, "No accounts found.");
            System.out.println(" "); // space
        }

        // iterate over them and print each
        for (Account account : userAccounts) {
            printer.printColoredLine(Printer.YELLOW, String.format("Account Name: %s, Account Number: %s, IBAN: %s, Type: %s, Balance: %.3f %s, Account Active: %s",
                    account.getAccountName().substring(0, 1).toUpperCase() + account.getAccountName().substring(1).toLowerCase(), // capitalize first letter
                    account.getAccountNumber(),
                    account.getIban(),
                    account.getAccountType().toString(),
                    account.getBalance(),
                    account.getCurrency(),
                    account.isActive() ? "Yes" : "No"
            ));
            System.out.println(" "); // space below each account
        }
    }

    // function to format the print of accounts to transfer
    public void printAccountsToTransfer(List<Account> accounts) {
        // iterate over the accounts
        for (Account account : accounts) {
            var f = String.format("%s %s (%s %s)",
                    account.getAccountName(),
                    account.getAccountNumber(),
                    account.getCurrency(),
                    account.getAccountType().toString().toLowerCase()
            );

            printer.printColoredLine(Printer.YELLOW, f);
            System.out.println(" "); // space below each account
        }


    }

    // function to perform deposit operation
    public void deposit(User user, Account account, double amount) {
        // store the post balance
        var balance = account.getBalance();

        // deposit the money to the account
        account.deposit(amount);

        // if the balance is positive make the account active and overdraft count to zero
        if (account.getBalance() >= 0) {
            account.setActive(true); // account is active
            account.setOverdraftCount(0); // reset count state
        }

        // save the changes
        var success = accountRepository.updateAccountRecord(account);

        // send message to the user
        if (success) {
            // create transaction record
            transactionService.createTransaction(
                    user,
                    account,
                    TransactionType.DEPOSIT,
                    amount,
                    balance
            );

            // print message
            successOperationMessage(TransactionType.DEPOSIT, account, amount);
        } else {
            printer.printError("Failed to deposit " + amount + ".");
        }
    }

    // function to perform withdraw operation
    public void withdraw(User user, Account account, double amount) {
        // check if the account is deactivated
        if (!account.isActive()) {
            printer.printColored(Printer.RED, "Account " + account.getAccountNumber() + " is deactivated. To reactivate the account you must resolve the negative balance and pay the overdraft fees.");
            return; // do not do anything
        }

        // get the balance of the account
        var balance = account.getBalance();

        // if the balance is negative or the amount is greater than the balance active the overdraft mechanism
        if (balance < 0 || amount > balance) {
            // if the amount is less than 100
            if (amount <= 100) {
                // withdraw with fees ( -amount -(50) + - 35 = -85)
                account.withdraw(amount);
                account.withdraw(35); // take the fees (-50 - (+35))

                // increase the overdraft counter
                account.setOverdraftCount(account.getOverdraftCount() + 1);

                // if the overdraft is 2
                if (account.getOverdraftCount() >= 2) {
                    // deactivate the account
                    account.setActive(false);
                }
            } else {
                printer.printError("Account " + account.getAccountNumber() + " is not enough balance.");
            }
        } else {
            // normally withdraw from the account
            account.withdraw(amount);
        }

        // save the changes
        var success = accountRepository.updateAccountRecord(account);

        if (success) {
            // create transaction record
            transactionService.createTransaction(
                    user,
                    account,
                    TransactionType.WITHDRAW,
                    amount,
                    balance
            );

            /// send withdraw message
            successOperationMessage(TransactionType.WITHDRAW, account, amount);
        }
    }

    // function to perform transfer operation
    public void transfer(
            User receiver,
            String receiverAccountNumber,
            Account senderAccount,
            double amount
    ) {
        // check if the senderAccount is deactivated
        if (!senderAccount.isActive()) {
            printer.printColored(Printer.RED, "Account " + senderAccount.getAccountNumber() + " is deactivated. To reactivate the senderAccount you must resolve the negative balance and pay the overdraft fees.");
            return; // do not do anything
        }

        // if the amount to transfer is more than the balance return error
        if (senderAccount.getBalance() < amount) {
            printer.printColored(Printer.RED, "Insufficient funds.");
            return; // do not do anything
        }

        // get the senderAccount of the targeted targetUser
        var receiverAccounts = getUserAccounts(receiver);

        // iterate over the accounts
        for (Account receiverAccount : receiverAccounts) {
            if (receiverAccount.getAccountNumber().equals(receiverAccountNumber)) {
                // withdraw from the main senderAccount
                senderAccount.setBalance(senderAccount.getBalance() - amount);

                // get the currencies
                var currencies = currencyRepository.getCurrencies();
                var senderRate = 0d;
                var receiverRate = 0d;

                // iterate over them
                for (Currency currency : currencies) {
                    // set the sender exchange rate
                    if (currency.currencyCode().equals(senderAccount.getCurrency())) {
                        senderRate = currency.exchangeRate();
                    }

                    // set the receiver exchange rate
                    if (currency.currencyCode().equals(receiverAccount.getCurrency())) {
                        receiverRate = currency.exchangeRate();
                    }
                }

                // calculate the rate between (amount * (senderRate/receiverRate))
                var receiverAmount = amount * (senderRate / receiverRate);

                // deposit it into the targeted targetUser
                receiverAccount.setBalance(receiverAccount.getBalance() + receiverAmount);

                // save the changes for both accounts
                var senderSuccess = accountRepository.updateAccountRecord(senderAccount);
                var receiverSuccess = accountRepository.updateAccountRecord(receiverAccount);

                // If the sender is successfully done print success message
                if (senderSuccess) {
                    successTransferMessage(
                            senderAccount,
                            receiverAccount,
                            amount
                    );
                }

                // todo: If the receiver is successfully done put notification
                if (receiverSuccess) {
                    // set notification for the targeted targetUser
                }
            }
        }
    }

    // function to validate account number
    public String validateAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            return "Account number must not be empty.";
        }

        // if it's not 14
        if (accountNumber.length() != 14) {
            return "Account number must have a length of 14 characters.";
        }

        return "";
    }
}
