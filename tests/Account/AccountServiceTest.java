package Account;

import User.User;
import User.UserRole;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceTest {

    AccountService accountService;
    User user;

    @BeforeEach
    public void setUp() {
        accountService = new AccountService();
        user = new User(
                1L,
                "Unit",
                "Test",
                "unittest@gmail.com",
                "hashedPassword123",
                UserRole.Customer,
                0,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        ); // hard coded user
    }

    @Test
    @DisplayName("Should create an account and store it in the system")
    void shouldCreateAccountAndStoreItInTheSystemAndReturnTrue() {
        // Arrange
        var currency = "BHD"; // hard coded currency
        var accountName = "Wesam BHD";

        // Act
        var success = accountService.createAccount(user, AccountType.Savings, currency, accountName);

        // Assert
        assertTrue(success);
    }

    @Test
    @DisplayName("Should not create an account and return false if the currency is not in the system")
    void shouldNotCreateAccountAndReturnFalseIfTheCurrencyIsNotInTheSystem() {
        // Arrange
        var currency = "XAF"; // not in the system
        var accountName = "Currency not in the system";

        // Act
        var success = accountService.createAccount(user, AccountType.Savings, currency, accountName);

        // Assert
        assertFalse(success);
    }

    @Test
    @DisplayName("Should return false when an account with same currency and type already exists")
    void shouldReturnFalseWhenAnAccountWithSameCurrencyAndTypeAlreadyExists() {
        // Arrange
        var currency = "BHD";
        var accountName = "Duplicate Account";

        // Act
        var success = accountService.createAccount(user, AccountType.Savings, currency, accountName);

        // Assert
        assertFalse(success); // false because the user has an account with the same currency and type
    }

    @Test
    @DisplayName("Should return user accounts in an array")
    void shouldReturnUserAccountsInAnArray() {
        // Act
        var accountsArray = accountService.getUserAccounts(user);
//        System.out.println(accountsArray);


        // Assert
        assertNotNull(accountsArray);
    }

    @Test
    @DisplayName("Should return an array with zero length to indicate the user has zero accounts")
    void shouldReturnAnArrayWithZeroLengthToIndicateTheUserHasZeroAccounts() {
        // Arrange
        var newUser = new User(
                53545L,
                "Special",
                "Account",
                "thisUserHasZeroAccounts@gmail.com",
                "hashedPassword123",
                UserRole.Customer,
                0,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        ); // hard coded user

        // Act
        var accountsArray = accountService.getUserAccounts(newUser);
//        System.out.println(accountsArray);

        // Arrange
        assertEquals(0, accountsArray.size());
    }

    @Test
    @DisplayName("Should return an integer of the user accounts array length")
    void shouldReturnAnIntegerOfTheUserAccountsArrayLength() {
        // Act
        var accountCount = accountService.userAccountsCount(user);
//        System.out.println(accountCount);

        // Assert
        assertNotNull(accountCount);
    }

    @Test
    @DisplayName("Should delete an account by account number")
    void shouldDeleteAccountByAccountNumber() {
        // Arrange
        var userAccountsArray = accountService.getUserAccounts(user);
        if (!userAccountsArray.isEmpty()) {
            var account = userAccountsArray.get(0); // get first account in the array

            // Act
            accountService.deleteAccount(user.getId(), account.getAccountNumber());

            // Assert
            assertEquals(userAccountsArray.size() - 1, accountService.userAccountsCount(user));
        }
    }

    @Test
    @DisplayName("Should print the user accounts in the terminal")
    void shouldPrintUserAccounts() {
        // Act
        accountService.printUserAccounts(user);
    }

    @Test
    @DisplayName("Should print a message that the user has no accounts")
    void shouldPrintAMessageThatTheUserHasNoAccounts() {
        // Arrange
        var newUser = new User(
                53545L,
                "Special",
                "Account",
                "thisUserHasZeroAccounts@gmail.com",
                "hashedPassword123",
                UserRole.Customer,
                0,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        ); // hard coded user

        // Act
        accountService.printUserAccounts(newUser);
    }

    @Test
    @DisplayName("Should deposit money to the account")
    void shouldDepositMoneyToTheAccount() {
        // Arrange
        var userAccount = accountService.getUserAccounts(user).get(0);

        // Act
        accountService.deposit(user, userAccount, 10);
    }

    @Test
    @DisplayName("Should withdraw money from the account")
    void shouldWithdrawMoneyFromTheAccount() {
        // Arrange
        var userAccount = accountService.getUserAccounts(user).get(0);

        // Act
        accountService.withdraw(user, userAccount, 5);
    }

    @Test
    @DisplayName("Should transfer money between two accounts")
    void shouldTransferMoneyBetweenTwoAccounts() {
        // Arrange
        var userAccount = accountService.getUserAccounts(user).get(2);
        var userAccount2 = accountService.getUserAccounts(user).get(0);

        // Act
        accountService.transfer(user, userAccount, userAccount2.getAccountNumber(), 1);
    }

    @AfterEach
    public void tearDown() {
        accountService = null;
    }

}