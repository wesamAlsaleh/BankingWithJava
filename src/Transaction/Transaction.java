package Transaction;

import java.util.UUID;

public class Transaction {
    private UUID id;
    private String accountNumber;
    private TransactionType transactionType;
    private String transactionDate;
    private double amount;
    private double postBalance;
}
