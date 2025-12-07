package Transaction;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private String accountName;
    private String accountNumber;
    private String iban;
    private TransactionType transactionType;
    private double amount;
    private double postBalance;
    private LocalDateTime transactionDate;
}
