package Transaction;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private String accountNumber;
    private TransactionType transactionType;
    private LocalDateTime transactionDate;
    private double amount;
    private double postBalance;
}
