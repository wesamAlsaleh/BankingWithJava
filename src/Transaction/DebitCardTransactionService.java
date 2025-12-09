package Transaction;

import Account.Account;
import Card.DebitCardService;
import Currency.CurrencyService;
import Global.Utils.Printer;
import User.User;
import Account.AccountService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class DebitCardTransactionService {
    private final DebitCardTransactionRepository debitCardTransactionRepository = new DebitCardTransactionRepository();
    private final AccountService accountService = new AccountService();
    private final CurrencyService currencyService = new CurrencyService();
    private final DebitCardService debitCardService = new DebitCardService();
    private final Printer printer = new Printer();

    // function to create debit card transaction record
    public boolean createDebitCardTransaction(Account account, String cardNumber, TransactionType transactionType, double amountInUsd) {
        // create debit card transaction obj
        var transaction = new DebitCardTransaction(
                account.getUserId(),
                account.getAccountNumber(),
                cardNumber,
                transactionType,
                amountInUsd
        );

        try {
            // create the file
            debitCardTransactionRepository.saveNewDebitCardTransactionRecord(transaction);

            return true; // return success
        } catch (Exception e) {
            printer.printError(e.getMessage());
            return false; // return failer
        }
    }

    // function to get user debit card transactions records
    private List<DebitCardTransaction> getUserDebitCardTransactions(User user) {
        return debitCardTransactionRepository.getUserDebitCardTransactions(user.getId());
    }

    // function to deposit money using debit card
    public void depositMoney(String cardNumber, double amount) {
        // todo: check card limitation

        // get the debit card
        var debitCard = debitCardService.getDebitCardByCardNumber(cardNumber);

        // if not found in the system return error
        if (debitCard == null) {
            printer.printError("Debit card does not exist");
            return;
        }

        // get the account for the account number
        var account = accountService.getAccountByAccountNumber(debitCard.getAccountNumber());

        // if account not found return error
        if (account == null) {
            printer.printError("Account does not exist");
            return;
        }

        // deposit to the account
        accountService.deposit(account, amount);

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
        createDebitCardTransaction(
                account,
                cardNumber,
                TransactionType.DEPOSIT,
                amountInUsd
        );
    }

    //todo:  function to format the printer for the transaction records
//    private void printer(List<DebitCardTransaction> transactions) {
//        // iterate over the transactions
//        for (DebitCardTransaction transaction : transactions) {
//            // print the operation symbol
//            switch (transaction.getTransactionType()) {
//                case DEPOSIT:
//                    printer.printColored(Printer.GREEN, "DEPOSIT++ ");
//                    System.out.printf("%.3f into %s REF %s at %s",
//                            transaction.getAmount(),
//                            transaction.getAccountName(),
//                            transaction.getIban(),
//                            transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
//                    );
//                    System.out.println(" "); // new line
//                    break;
//                case WITHDRAW:
//                    printer.printColored(Printer.RED, "WITHDRAW-- ");
//                    System.out.printf("%.3f from %s REF %s at %s",
//                            transaction.getAmount(),
//                            transaction.getAccountName(),
//                            transaction.getIban(),
//                            transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
//                    );
//                    System.out.println(" "); // new line
//                    break;
//                case TRANSFER:
//                    printer.printColored(Printer.PURPLE, "TRANSFER~~ ");
//                    System.out.printf("%.3f from %s REF %s at %s",
//                            transaction.getAmount(),
//                            transaction.getAccountName(),
//                            transaction.getIban(),
//                            transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
//                    );
//                    System.out.println(" "); // new line
//                    break;
//            }
//        }
//        System.out.println(" "); // space after last record
//    }

    //todo:  print the user debit card transactions
//    public void printUserDebitCardTransactions(User user, DateFilter dateFilter, String operation) {
//        // get the user transactions
//        var transactions = getUserTransactions(user);
//
//        // if no transactions return message
//        if (transactions.isEmpty()) {
//            printer.printWarning("There are no transactions in this account");
//            return; // do nothing
//        }
//
//        // filter date holder
//        LocalDateTime date;
//
//        // filter the transactions based on the date
//        switch (dateFilter) {
//            case TODAY:
//                date = LocalDateTime.now().toLocalDate().atStartOfDay();
//                break;
//            case YESTERDAY:
//                date = LocalDateTime.now().minusDays(1).toLocalDate().atStartOfDay();
//                break;
//            case LAST_WEEK:
//                date = LocalDateTime.now().minusDays(7).toLocalDate().atStartOfDay();
//                break;
//            case LAST_MONTH:
//                date = LocalDateTime.now().minusMonths(1).toLocalDate().atStartOfDay();
//                break;
//            case ALL:
//                date = transactions.get(0).getTransactionDate().toLocalDate().atStartOfDay(); // get the first transaction date
//            default:
//                throw new IllegalArgumentException("Unknown date filter type");
//        }
//
//        // filter based on the date
//        var filteredTransactions = transactions.stream()
//                .filter(t -> t.getTransactionDate().isAfter(date))
//                .toList();
//
//        // if the filter result is empty show message
//        if (filteredTransactions.isEmpty()) {
//            printer.printWarning("There are no transactions in this account");
//            return; // do not print anything else
//        }
//
//        // print all the transactions
//        if (operation.equals("ALL")) {
//            printer(filteredTransactions);
//        }
//
//        // refilter the transactions bases on the type
//        var filteredTransactions2 = filteredTransactions.stream()
//                .filter(t -> operation.equals(t.getTransactionType().toString()))
//                .toList();
//
//        // if the filter result is empty show message
//        if (filteredTransactions2.isEmpty()) {
//            printer.printWarning("There are no transactions in this account");
//            return; // do not print anything else
//        }
//
//        // print them
//        printer(filteredTransactions2);
//    }

}
