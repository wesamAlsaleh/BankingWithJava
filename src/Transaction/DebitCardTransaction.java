package Transaction;

import java.time.LocalDateTime;
import java.util.UUID;

public class DebitCardTransaction {
    private String id; // uuid
    private Long userId;
    private String accountNumber;
    private String cardNumber;
    private TransactionType transactionType;
    private double amountInUsd;
    private LocalDateTime transactionDate;

    // default constructor
    public DebitCardTransaction(Long userId, String accountNumber,String cardNumber, TransactionType transactionType, double amountInUsd) {
        this.id = UUID.randomUUID().toString(); // uuid
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.cardNumber = cardNumber;
        this.transactionType = transactionType;
        this.amountInUsd = amountInUsd; //
        this.transactionDate = LocalDateTime.now();
    }

    // custom constructor


    public DebitCardTransaction(String id, Long userId, String accountNumber, String cardNumber, TransactionType transactionType, double amountInUsd, LocalDateTime transactionDate) {
        this.id = id;
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.cardNumber = cardNumber;
        this.transactionType = transactionType;
        this.amountInUsd = amountInUsd;
        this.transactionDate = transactionDate;
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

    public String getCardNumber() {
        return cardNumber;
    }

    public double getAmountInUsd() {
        return amountInUsd;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    // function to create debit card transaction record
    public String createTransactionRecord() {
        return String.format("UUID:%s, User_Id:%s, Account_Number:%s, Card_Number:%s, Transaction_Type:%s, Transaction_Amount_USD:%.3f, Transaction_Date:%s",
                getId(),
                getUserId(),
                getAccountNumber(),
                getCardNumber(),
                getTransactionType(),
                getAmountInUsd(),
                getTransactionDate()
        );
    }

    @Override
    public String toString() {
        return "DebitCardTransaction{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", accountNumber='" + accountNumber + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", transactionType=" + transactionType +
                ", amountInUsd=" + amountInUsd +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
