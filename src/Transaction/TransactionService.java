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

    // print the user transactions
    public void printUserTransactions(User user) {
        // get the user transactions
        var transactions = getUserTransactions(user);

        // if no transactions return message
        if (transactions.isEmpty()) {
            printer.printWarning("There are no transactions in this account");
            return; // do nothing
        }

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
            System.out.println(" "); // space
        }
    }
}
