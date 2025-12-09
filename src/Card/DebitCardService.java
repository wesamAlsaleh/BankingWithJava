package Card;

import Account.Account;
import Account.AccountService;
import Currency.CurrencyService;
import Global.Utils.Printer;
import Transaction.DebitCardTransaction;
import Transaction.DebitCardTransactionService;
import Transaction.TransactionType;
import User.UserRepository;

import java.util.List;

public class DebitCardService {
    private final DebitCardRepository debitCardRepository = new DebitCardRepository();
    private final Printer printer = new Printer();
    private final AccountService accountService = new AccountService();
    private final CurrencyService currencyService = new CurrencyService();
    private final DebitCardTransactionService debitCardTransactionService = new DebitCardTransactionService();
    private final UserRepository userRepository = new UserRepository();

    // function to generate card number
    private String generateCardNumber() {
        // card number holder
        StringBuilder cardNumber = new StringBuilder();

        // if the number is existed generate again
        do {
            // generate 16 random digits
            for (int i = 0; i < 16; i++) {
                cardNumber.append((int) (Math.random() * 10)); // random 0 to 9
            }

        } while (debitCardRepository.isCardNumberUnique(cardNumber.toString()));

        // save the number in the system
        debitCardRepository.writeCardNumber(cardNumber.toString());

        // return the number
        return cardNumber.toString();
    }

    // function to generate debit card transaction record
    private void generateDebitCardTransaction(Account account, double amount, String cardNumber, TransactionType transactionType) {
        // get usd exchange rate for the account currency
        var usdExchangeRate = currencyService.getUsdRate(account.getCurrency());

        // if the currency not available return error
        if (usdExchangeRate == 0) {
            printer.printError("Cannot deposit to this account due to currency exchange rate");
            return; // do nothing
        }

        // convert the amount to usd
        var amountInUsd = amount * usdExchangeRate;

        // create debit card transaction record
        debitCardTransactionService.createDebitCardTransaction(
                account,
                cardNumber,
                transactionType,
                amountInUsd
        );
    }

    // function to check if the card exceeded its limit
    private boolean exceededTheLimit(DebitCard debitCard, Account consumerAccount, TransactionType transactionType, double amount) {
        // get the limit of this operation of this type of cards
        var limit = debitCard.getLimit(transactionType);

        // convert the amount to usd
        var usdExchangeRate = currencyService.getUsdRate(consumerAccount.getCurrency());

        // if the usd exchange rate is 0 return error
        if (usdExchangeRate == 0) {
            printer.printError("Cannot deposit to this account due to currency exchange rate");
            return false;
        }

        // convert the amount to usd
        amount = amount * usdExchangeRate;

        System.out.println("Amount in USD " + amount);

        // calculate the available limit
        var availableLimit = limit - amount;

        System.out.println("Limit is " + limit + " and availableLimit is " + availableLimit);

        // get the card total spent today
        var sumAmount = debitCardTransactionService.getAmountSpentInUSD(debitCard);

        System.out.println("Sum amount is " + sumAmount);

        // if the sum amount is less than the limit (return false to mean ok)
        if (sumAmount < limit) { // both usd!
            // if the amount to pay is less or equal the available (return false to mean ok)
            if (amount <= availableLimit) {
                return false;
            }
        }

        // return exceeded
        return true;
    }

    // function to create a debit card
    public void createDebitCard(Long userId, String accountNumber, DebitCardType type) {
        // generate card number
        var cardNumber = generateCardNumber();

        // get the user cards
        var userCards = debitCardRepository.getUserCards(userId);

        // if the user has a debit card with the same account number return error
        for (DebitCard card : userCards) {
            if (card.getAccountNumber().equals(accountNumber)) {
                printer.printError("Debit Card already exists for this account!");
                return; // do nothing
            }
        }

        // create card instance
        var card = new DebitCard(
                userId,
                accountNumber,
                cardNumber,
                type
        );

        // save the card in the system
        var success = debitCardRepository.saveCard(card);

        // if successfully put message
        if (success) {
            printer.printSuccessful("Card created successfully!");

            // todo: put notification
        } else {
            printer.printError("Failed to create card!");
        }
    }

    // function to print user cards
    public void printUserCards(Long userId) {
        // get the user cards
        var userCards = debitCardRepository.getUserCards(userId);

        // if no cards print error
        if (userCards.isEmpty()) {
            printer.printError("No user cards found!");
            return; // do nothing
        }

        // format the print
        for (DebitCard card : userCards) {
            printer.printColoredLine(Printer.YELLOW, String.format("Card number: %s (%s) linked to account number: %s",
                    card.getCardNumber(),
                    card.getType().toString().toLowerCase(),
                    card.getAccountNumber()
            ));
            System.out.println(" "); // line after each
        }
    }

    // function to get the user cards
    public List<DebitCard> getUserCards(Long userId) {
        return debitCardRepository.getUserCards(userId);
    }

    // function to get card details by card number
    public DebitCard getDebitCardByCardNumber(String cardNumber) {
        return debitCardRepository.getDebitCardByCardNumber(cardNumber);
    }

    // function to deposit money using debit card
    public void depositMoney(String cardNumber, double amount) {
        // get the debit card
        var debitCard = getDebitCardByCardNumber(cardNumber);

        // if not found in the system return error
        if (debitCard == null) {
            printer.printError("Debit card does not exist");
            return;
        }

        // get the account by the account number
        var account = accountService.getAccountByAccountNumber(debitCard.getAccountNumber());

        // if account not found return error
        if (account == null) {
            printer.printError("Account does not exist");
            return;
        }

        // check card limitation
        var exceededTheLimit = exceededTheLimit(debitCard, account, TransactionType.DEPOSIT, amount);

        // if the usage exceeded the limit return error
        if (exceededTheLimit) {
            printer.printError("Debit card amount exceeded the limit");
            return; // do nothing
        }

        // deposit to the account
        var success = accountService.deposit(account, amount);

        // generate the record only when the operation is success
        if (success) {
            // create transaction record
            generateDebitCardTransaction(account, amount, cardNumber, TransactionType.DEPOSIT);
        }
    }

    // function to withdraw money using debit card
    public void withdrawMoney(String cardNumber, double amount) {
        // get the card details
        var debitCard = getDebitCardByCardNumber(cardNumber);

        // if the card is not available return error
        if (debitCard == null) {
            printer.printError("Debit card does not exist");
            return;
        }

        // get the account to withdraw
        var account = accountService.getAccountByAccountNumber(debitCard.getAccountNumber());

        // if the account is not available return error
        if (account == null) {
            printer.printError("Account does not exist");
            return;
        }

        // check card limitation
        var exceededTheLimit = exceededTheLimit(debitCard, account, TransactionType.DEPOSIT, amount);

        // if the usage exceeded the limit return error
        if (exceededTheLimit) {
            printer.printError("Debit card amount exceeded the limit");
            return; // do nothing
        }

        // withdraw the money
        var success = accountService.withdraw(account, amount);

        // generate the record only when the operation is success
        if (success) {
            // create transaction record
            generateDebitCardTransaction(account, amount, cardNumber, TransactionType.WITHDRAW);
        }
    }

    // function transfer money from account to account using debit card
    public void transferMoney(String cardNumber, String receiverAccountNumber, double amount) {
        // get the sender card details
        var debitCard = getDebitCardByCardNumber(cardNumber);

        // if the card is not available return error
        if (debitCard == null) {
            printer.printError("Debit card does not exist");
            return;
        }

        // get the sender account using the card number
        var senderAccount = accountService.getAccountByAccountNumber(debitCard.getAccountNumber());

        // if the account is not available return error
        if (senderAccount == null) {
            printer.printError("Sender account does not exist");
            return;
        }

        // set the operation
        TransactionType transactionType = TransactionType.TRANSFER;

        // get the receiver account by accountNumber
        var receiverAccount = accountService.getAccountByAccountNumber(receiverAccountNumber);

        // if the account is not available return error
        if (receiverAccount == null) {
            printer.printError("Receiver account does not exist");
        }

        // get the user to receive money
        var userToReceive = userRepository.getUserById(receiverAccount.getUserId());

        // if the user is not available return error
        if (userToReceive == null) {
            printer.printError("Receiver user does not exist");
            return;
        }

        // check if the user id is the same as sender user id
        if (userToReceive.getId().equals(senderAccount.getUserId())) {
            // set the operation type to self transfer
            transactionType = TransactionType.TRANSFER_OWN;
        }

        // check card limitation
        var exceededTheLimit = exceededTheLimit(debitCard, senderAccount, transactionType, amount);

        // if the usage exceeded the limit return error
        if (exceededTheLimit) {
            printer.printError("Debit card amount exceeded the limit");
            return; // do nothing
        }

        // perform the operation
        var success = accountService.transfer(
                userToReceive,
                receiverAccount.getAccountNumber(),
                senderAccount,
                amount
        );

        // if successfully generate debit card transaction record
        if (success) {
            generateDebitCardTransaction(receiverAccount, amount, cardNumber, transactionType);
        }
    }

    // function to validate card number
    public String validateCardNumber(String cardNumber) {
        // if the card number is not 14 return error
        if (cardNumber.length() != 16) {
            return "Invalid card number, it must be of length 16";
        }

        // if card number contains letters return error
        for (char c : cardNumber.toCharArray()) {
            // if the character is digit return error
            if (!Character.isDigit(c)) {
                return "Invalid card number";
            }
        }

        // pass
        return "";
    }
}
