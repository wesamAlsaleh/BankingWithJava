package Transaction;

import Account.Account;
import Global.Utils.Printer;
import User.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class TransactionService {
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final Printer printer = new Printer();

    // function to create transaction record
    public boolean createTransaction(Account account, TransactionType transactionType, double amount, double postBalance) {
        // create transaction obj
        var transaction = new Transaction(
                UUID.randomUUID().toString(),
                account.getUserId(),
                account.getAccountName(),
                account.getAccountNumber(),
                account.getIban(),
                transactionType,
                amount,
                postBalance,
                LocalDateTime.now()
        );

        try {
            // create the file
            transactionRepository.saveNewTransactionRecord(transaction);

            return true; // return success
        } catch (Exception e) {
            printer.printError(e.getMessage());
            return false; // return failer
        }
    }

    // function to get user transactions records
    private List<Transaction> getUserTransactions(User user) {
        return transactionRepository.getUserTransactions(user.getId());
    }

    // function to format the printer for the transaction records
    private void printer(List<Transaction> transactions) {
        // iterate over the transactions
        for (Transaction transaction : transactions) {
            // print the operation symbol
            switch (transaction.getTransactionType()) {
                case DEPOSIT:
                    printer.printColored(Printer.GREEN, "DEPOSIT++ ");
                    break;
                case WITHDRAW:
                    printer.printColored(Printer.RED, "WITHDRAW-- ");
                    break;
                case TRANSFER:
                    printer.printColored(Printer.PURPLE, "TRANSFER~~ ");
                    break;
            }
            System.out.printf("%f into %s (%s) at %s",
                    transaction.getAmount(),
                    transaction.getAccountName(),
                    transaction.getIban(),
                    transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            );
            System.out.println(" "); // new line
        }

        System.out.println(" "); // space after last record
    }

    // print the user transactions
    public void printUserTransactions(User user, DateFilter dateFilter) {
        System.out.println("Im in this function");

        // get the user transactions
        var transactions = getUserTransactions(user);

        // if no transactions return message
        if (transactions.isEmpty()) {
            printer.printWarning("There are no transactions in this account");
            return; // do nothing
        }

        // filter date holder
        LocalDateTime date;

        System.out.println("im here 1");

        // filter the transactions based on the date
        switch (dateFilter) {
            case TODAY:
                date = LocalDateTime.now().toLocalDate().atStartOfDay();
                break;
            case YESTERDAY:
                date = LocalDateTime.now().minusDays(1).toLocalDate().atStartOfDay();
                break;
            case LAST_WEEK:
                date = LocalDateTime.now().minusDays(7).toLocalDate().atStartOfDay();
                break;
            case LAST_MONTH:
                date = LocalDateTime.now().minusMonths(1).toLocalDate().atStartOfDay();
                break;
            case ALL:
                date = transactions.get(0).getTransactionDate().toLocalDate().atStartOfDay(); // get the first transaction date
            default:
                throw new IllegalArgumentException("Unknown date filter type");
        }

        System.out.println("im here 2");


        // filter based on the date
        var filteredTransactions = transactions.stream()
                .filter(t -> t.getTransactionDate().isAfter(date))
                .toList();

        System.out.println("im here 3");
        System.out.println(filteredTransactions);


        // if the filter result is empty show message
        if (filteredTransactions.isEmpty()) {
            printer.printWarning("There are no transactions in this account");
            return; // do not print anything else
        }

        System.out.println("im here 4");


        // print the transactions
        printer(filteredTransactions);
    }

    // function to get the system
    private List<Transaction> getSystemTransactions() {
        return transactionRepository.getTransactions();
    }

    // function to print the system
    public void printSystemTransactions() {
        // get the transactions
        var transactions = getSystemTransactions();

        // if no transactions
        if (transactions.isEmpty()) {
            printer.printWarning("There are no transactions in the system.");
        }

        // print the transactions
        printer(transactions);
    }
}
