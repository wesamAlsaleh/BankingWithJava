package Account;

import java.time.LocalDateTime;

public class Account implements IAccount {
    private Long userId;
    private String accountNumber;
    private String iban;
    private AccountType accountType;
    private String currency;
    private double balance;
    private int overdraftCount;
    private boolean isActive;
    private LocalDateTime createdAt;

    // default constructor
    public Account(Long userId, String accountNumber, String iban, AccountType accountType, String currency) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.iban = iban;
        this.accountType = accountType;
        this.currency = currency;
        this.balance = 0;
        this.overdraftCount = 0;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }

    // custom constructor
    public Account(Long userId, String accountNumber, String iban, AccountType accountType, String currency, double balance, int overdraftCount, boolean isActive, LocalDateTime createdAt) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.iban = iban;
        this.accountType = accountType;
        this.currency = currency;
        this.balance = balance;
        this.overdraftCount = overdraftCount;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    @Override
    public void deposit(double amount) {

    }

    @Override
    public void withdraw(double amount) {

    }

    @Override
    public void transfer(String accountNumber, double amount) {
    }

    // getters
    public Long getUserId() {
        return userId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getIban() {
        return iban;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public String getCurrency() {
        return currency;
    }

    public double getBalance() {
        return balance;
    }

    public int getOverdraftCount() {
        return overdraftCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // function to create account record
    public String accountRecord() {
        return String.format(
                "user_id:%s, account_number:%s, iban:%s, account_type:%s, currency:%s, balance:%f, overdraft_count:%d, is_active:%B, created_at:%s",
                userId,
                accountNumber,
                iban,
                accountType,
                currency,
                balance,
                overdraftCount,
                isActive,
                createdAt
        );
    }

    @Override
    public String toString() {
        return "Account{" +
                "userId=" + userId +
                ", accountNumber='" + accountNumber + '\'' +
                ", iban='" + iban + '\'' +
                ", accountType=" + accountType +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                ", overdraftCount=" + overdraftCount +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}
