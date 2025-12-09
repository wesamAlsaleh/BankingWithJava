package Account;

import Currency.Currency;
import Currency.CurrencyService;
import Currency.CurrencyRepository;
import Global.Utils.Printer;
import Transaction.TransactionService;
import Transaction.TransactionType;
import User.User;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AccountService {
    private final AccountRepository accountRepository = new AccountRepository();
    private final CurrencyRepository currencyRepository = new CurrencyRepository();
    private final TransactionService transactionService = new TransactionService();
    private final CurrencyService currencyService = new CurrencyService();
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
                printer.printColoredLine(Printer.YELLOW, String.format("Success! DEPOSIT++ of %s %.2f to %s REF IBAN *****%s is completed on %s Balance %s %.2f",
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
                printer.printColoredLine(Printer.YELLOW, String.format("Success! WITHDRAW-- of %s %.2f from %s REF IBAN *****%s is completed on %s Balance %s %.2f",
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

    // function to perform deposit operation
    public boolean deposit(Account account, double amount) {
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
                    account,
                    TransactionType.DEPOSIT,
                    amount,
                    balance
            );

            // print message
            successOperationMessage(TransactionType.DEPOSIT, account, amount);

            // todo: put notification
        } else {
            printer.printError("Failed to deposit " + amount + ".");
        }

        return success;
    }

    // function to perform withdraw operation
    public boolean withdraw(Account account, double amount) {
        // check if the account is deactivated
        if (!account.isActive()) {
            printer.printColored(Printer.RED, "Account " + account.getAccountNumber() + " is deactivated. To reactivate the account you must resolve the negative balance and pay the overdraft fees.");
            return false; // do not do anything
        }

        // get the balance of the account
        var balance = account.getBalance();

        // get the USD exchange rate
        var usdExchangeRate = currencyService.getUsdRate(account.getCurrency());

        // if usd rate not available return error
        if (usdExchangeRate == 0) {
            printer.printError("Currency " + account.getAccountNumber() + " does not exist.");
        }

        // get the amount in usd
        var amountInUSD = (balance - amount) * usdExchangeRate;
        amountInUSD = Math.abs(amountInUSD); // make it positive

        // get the currencies
        var currencies = currencyService.getCurrencies();

        // exchange rate holder
        double exchangeRate = 0;

        // iterate over the currencies
        for (Currency currency : currencies) {
            if (currency.currencyCode().equals(account.getCurrency())) {
                // get the account currency exchange rate to USD
                exchangeRate = currency.exchangeRate();
                break; // no need to check
            }
        }

        // if account rate not available return error
        if (exchangeRate == 0) {
            printer.printError("Currency " + account.getAccountNumber() + " does not exist.");
            return false; // do nothing
        }

        // get the overdraft fees (35$) based on the account rate to USD
        var fees = 35 / exchangeRate; // ~13BHD

        // if the amount is greater than the balance active the overdraft mechanism
        if (amount > balance) {
            // if the amount needed in USD is less than 100$ perform the overdraft mechanism
            if (amountInUSD <= 100) {
                // withdraw with fees
                account.withdraw(amount); // ex: 0 - 25 = -25
                account.withdraw(fees); // ex: -25 - 13 = - 38

                // increase the overdraft counter
                account.setOverdraftCount(account.getOverdraftCount() + 1);

                // if the overdraft is 2
                if (account.getOverdraftCount() >= 2) {
                    // deactivate the account
                    account.setActive(false);
                }
            } else {
                printer.printError("Your balance is too low.");
                return false; // return failer
            }
        } else {
            // normally withdraw from the account
            account.withdraw(amount);
        }

        // save the changes
        var success = accountRepository.updateAccountRecord(account);

        // if the operation done
        if (success) {
            // create transaction record
            transactionService.createTransaction(
                    account,
                    TransactionType.WITHDRAW,
                    amount,
                    balance
            );

            /// send withdraw message
            successOperationMessage(TransactionType.WITHDRAW, account, amount);

            // todo: put notification
        } else {
            printer.printError("Failed to withdraw " + amount + ".");
        }

        // return success
        return success;
    }

    // function to perform transfer operation
    public boolean transfer(
            User receiver,
            String receiverAccountNumber,
            Account senderAccount,
            double amount
    ) {
        // check if the senderAccount is deactivated
        if (!senderAccount.isActive()) {
            printer.printColored(Printer.RED, "Account " + senderAccount.getAccountNumber() + " is deactivated. To reactivate the senderAccount you must resolve the negative balance and pay the overdraft fees.");
            return false; // do not do anything
        }

        // if the amount to transfer is more than the balance return error
        if (amount > senderAccount.getBalance()) {
            printer.printError("Insufficient funds.");
            return false; // do not do anything
        }

        // get the senderAccount of the targeted targetUser
        var receiverAccounts = getUserAccounts(receiver);

        // iterate over the accounts
        for (Account receiverAccount : receiverAccounts) {
            if (receiverAccount.getAccountNumber().equals(receiverAccountNumber)) {
                // get the balance of the sender
                var senderBalance = senderAccount.getBalance();

                // withdraw from the main senderAccount
                senderAccount.setBalance(senderBalance - amount);

                // get the currencies
                var currencies = currencyRepository.getCurrencies();
                var senderFlag = false;
                var receiverFlag = false;
                String senderCurrency = "";
                String receiverCurrency = "";

                // iterate over them
                for (Currency currency : currencies) {
                    // check if the sender has an exchange rate
                    if (currency.currencyCode().equals(senderAccount.getCurrency())) {
                        senderFlag = true; // set to trues
                        senderCurrency = senderAccount.getCurrency();
                    }

                    // check if the receiver has an exchange rate
                    if (currency.currencyCode().equals(receiverAccount.getCurrency())) {
                        receiverFlag = true; // set to trues
                        receiverCurrency = receiverAccount.getCurrency();
                    }
                }

                // if the currency is not in the system print message
                if (!senderFlag) {
                    printer.printError("Cannot transfer " + amount + " from this account in the moment.");
                    return false; // do nothing
                }

                if (!receiverFlag) {
                    printer.printError("Cannot transfer " + amount + " to this account in the moment.");
                    return false; // do nothing
                }

                // calculate the rate between
                var receiverAmount = currencyService.convertCurrency(senderCurrency, receiverCurrency, amount);

                // get the balance of the receiver
                var receiverBalance = receiverAccount.getBalance();

                // deposit it into the targeted targetUser
                receiverAccount.setBalance(receiverBalance + receiverAmount);

                // save the changes for both accounts
                var senderSuccess = accountRepository.updateAccountRecord(senderAccount);
                var receiverSuccess = accountRepository.updateAccountRecord(receiverAccount);

                // If the sender is successfully done
                if (senderSuccess) {
                    // create transaction record
                    transactionService.createTransaction(
                            senderAccount,
                            TransactionType.TRANSFER,
                            amount,
                            senderBalance
                    );

                    // print success message for the sender
                    printer.printColoredLine(Printer.YELLOW, String.format("Success! TRANSFER~~ of %s %.2f to %s REF IBAN *****%s is completed on %s Balance %s %.2f",
                            senderAccount.getCurrency(),
                            amount,
                            receiverAccount.getAccountName(),
                            receiverAccount.getIban().substring(21).replaceAll("\\s+", ""), // last 5 digits in the Iban (\\s removes white spaces, while the + mean one or more)
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                            senderAccount.getCurrency(),
                            senderAccount.getBalance()
                    ));

                    // todo: put notification
                }

                // If the receiver is successfully done
                if (receiverSuccess) {
                    // create transaction record
                    transactionService.createTransaction(
                            receiverAccount,
                            TransactionType.TRANSFER,
                            amount,
                            receiverBalance
                    );

                    // todo: put notification
//                    printer.printColoredLine(Printer.YELLOW, String.format("Success! TRANSFER~~ of %s %.2f from %s to %s REF IBAN *****%s is completed on %s Balance %s %s",
//                            senderAccount.getCurrency(),
//                            amount,
//                            senderAccount.getAccountName(),
//                            receiverAccount.getAccountName(),
//                            receiverAccount.getIban().substring(21).replaceAll("\\s+", ""), // last 5 digits in the Iban (\\s removes white spaces, while the + mean one or more)
//                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
//                            senderAccount.getCurrency(),
//                            senderAccount.getBalance()
//                    ));
                }
            }
        }

        // return success
        return true;
    }

    // function to get the account by account number
    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.getAccountByAccountNumber(accountNumber);
    }
}
