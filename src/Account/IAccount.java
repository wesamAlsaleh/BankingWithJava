package Account;

public interface IAccount {
    void deposit(double amount);

    void withdraw(double amount);

    void transfer(String accountNumber, double amount);
}
