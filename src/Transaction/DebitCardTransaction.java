package Transaction;

import java.time.LocalDateTime;

public class DebitCardTransaction {
    private String id; // uuid
    private Long userId;
    private String accountNumber;
    private TransactionType transactionType;
    private double amountInUsd;
    private LocalDateTime transactionDate;

    // default constructor
    public DebitCardTransaction(Long userId, String accountNumber, TransactionType transactionType, double amountInUsd) {
        this.userId = userId;
        this.transactionType = transactionType;
        this.accountNumber = accountNumber;
        this.amountInUsd = amountInUsd; //
        this.transactionDate = LocalDateTime.now();
    }

    // getters
    public String getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amountInUsd;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    // function to create debit card transaction record
    public String createTransactionRecord() {
        return String.format("UUID:%s, User_Id:%s, Account_Number:%s, Transaction_Type:%s, Transaction_Amount_USD:%.3f, Transaction_Date:%s",
                getId(),
                getUserId(),
                getAccountNumber(),
                getTransactionType(),
                getAmount(),
                getTransactionDate()
        );
    }

    @Override
    public String toString() {
        return "DebitCardTransaction{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", accountNumber='" + accountNumber + '\'' +
                ", transactionType=" + transactionType +
                ", amountInUsd=" + amountInUsd +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
