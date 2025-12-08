package Transaction;

import java.time.LocalDateTime;

public class Transaction {
    private String id; // uuid
    private Long userId;
    private String accountName;
    private String accountNumber;
    private String iban;
    private TransactionType transactionType;
    private double amount;
    private double postBalance;
    private LocalDateTime transactionDate;

    // default constructor
    public Transaction(String id, Long userId, String accountName, String accountNumber, String iban, TransactionType transactionType, double amount, double postBalance, LocalDateTime transactionDate) {
        this.id = id;
        this.userId = userId;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.iban = iban;
        this.transactionType = transactionType;
        this.amount = amount;
        this.postBalance = postBalance;
        this.transactionDate = transactionDate;
    }

    // getters
    public String getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getIban() {
        return iban;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public double getPostBalance() {
        return postBalance;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    // function to create transaction record
    public String createTransactionRecord() {
        return String.format("UUID:%s, User_Id:%s, Account_Name:%s, Account_Number:%s, Iban:%s, Transaction_Type:%s, Transaction_Amount:%.3f, Post_Balance:%.2f, Transaction_Date:%s",
                getId(),
                getUserId(),
                getAccountName(),
                getAccountNumber(),
                getIban(),
                getTransactionType(),
                getAmount(),
                getPostBalance(),
                getTransactionDate()
        );
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", accountName='" + accountName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", iban='" + iban + '\'' +
                ", transactionType=" + transactionType +
                ", amount=" + amount +
                ", postBalance=" + postBalance +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
