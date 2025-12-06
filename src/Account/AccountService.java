package Account;

import Global.Utils.Printer;
import User.User;

import java.math.BigInteger;
import java.util.List;

public class AccountService {
    private final AccountRepository accountRepository = new AccountRepository();
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

    // function to create account record
    public boolean createAccount(User user, AccountType accountType, String currency) {
        // generate a unique account number
        var accountNumber = generateAccountNumber();

        // generate iban
        var iban = generateIban(accountNumber);

        // create new account object
        var account = new Account(
                user.getId(),
                accountNumber,
                iban,
                accountType,
                currency
        );

        // prepare the file name as accountNumber-userId.txt
        var fileName = accountNumber + "-" + user.getId() + ".txt";

        // create new record
        return accountRepository.saveNewAccountRecord(fileName, account.accountRecord());
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
            printer.printColoredLine(Printer.YELLOW, String.format("Account Number: %s, IBAN: %s, Type: %s, Balance: %.3f %s, Account Active: %s",
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
}
