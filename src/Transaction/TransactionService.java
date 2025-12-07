package Transaction;

import Account.Account;
import Global.Utils.Printer;
import User.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionService {
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final Printer printer = new Printer();

    // function to create transaction record
    public boolean createTransaction(User user, Account account, TransactionType transactionType, double amount, double postBalance) {
        // create transaction obj
        var transaction = new Transaction(
                UUID.randomUUID(),
                user.getId(),
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
}
