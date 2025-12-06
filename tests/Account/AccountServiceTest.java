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

//    @Test
//    @DisplayName("Should generate unique account number and store it in a file")
//    void shouldGenerateUniqueAccountNumberAndStoreItInAFile() {
//        // Act
//        var accountNumber = accountService.generateAccountNumber();
//
//        System.out.println(accountNumber);
//
//        // Assert
//       assertNotNull(accountNumber);
//    }

//    @Test
//    @DisplayName("Should generate iban number and return it as string")
//    void shouldGenerateIbanNumberAndReturnItAsString() {
//        // Arrange
//        var accountNumber = accountService.generateAccountNumber();
//        // 535438431377 for less than 10
//
//        // Act
//        var checkDigit = accountService.generateCheckDigits(accountNumber);
//
//        // Assert
//        assertNotNull(checkDigit);
//    }

//    @Test
//    @DisplayName("Should generate unique iban based on the account number")
//    void shouldGenerateUniqueIban(){
//        // Arrange
//        var accountNumber = accountService.generateAccountNumber();
//
//        // Act
//        var iban = accountService.generateIban(accountNumber);
//
//        System.out.println(iban);
//
//        // Assert
//        assertNotNull(iban);
//    }

//    @Test
//    @DisplayName("Should create new account and create new record file in the db")
//    void shouldCreateNewAccountAndCreateNewRecordFileInTheDb() {
//        // Arrange
//        UserRepository userRepository = new UserRepository();
//        var user = userRepository.getUserByEmail("unittest@gmail.com");
//
//        // Act
//        accountService.createAccount(user, AccountType.Checking, "BHD");
//
//        // Assert
//        assertNotNull(user);
//    }

    @Test
    @DisplayName("Should create an account and store it in the system")
    void shouldCreateAccountAndStoreItInTheSystemAndReturnTrue(){
        // Arrange
        var currency = "USD"; // hard coded currency

        // Act
        var success = accountService.createAccount(user, AccountType.Savings, currency);

        // Assert
        assertTrue(success);
    }

    @Test
    @DisplayName("Should return user accounts in an array")
    void shouldReturnUserAccountsInAnArray(){
        // Act
        var accountsArray = accountService.getUserAccounts(user);

        // Assert
        assertNotNull(accountsArray);
    }

    @Test
    @DisplayName("Should return an array with zero length to indicate the user has zero accounts")
    void shouldReturnAnArrayWithZeroLengthToIndicateTheUserHasZeroAccounts(){
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

        // Arrange
        assertEquals(0, accountsArray.size());
    }

    @Test
    @DisplayName("Should return an integer of the user accounts array length")
    void shouldReturnAnIntegerOfTheUserAccountsArrayLength(){
        // Act
        var accountCount = accountService.userAccountsCount(user);
//        System.out.println(accountCount);

        // Assert
        assertNotNull(accountCount);
    }

    @Test
    @DisplayName("Should create an account by account number")
    void shouldDeleteAccountByAccountNumber(){
        // Arrange
        var userAccountsArray = accountService.getUserAccounts(user);
        var account = userAccountsArray.get(0); // get first account in the array

        // Act
        accountService.deleteAccount(user.getId(), account.getAccountNumber());

        // Assert
        assertEquals(userAccountsArray.size() - 1, accountService.userAccountsCount(user));
    }

    @Test
    @DisplayName("Should print the user accounts in the terminal")
    void shouldPrintUserAccounts(){
        // Act
        accountService.printUserAccounts(user);
    }

    @Test
    @DisplayName("Should print a message that the user has no accounts")
    void shouldPrintAMessageThatTheUserHasNoAccounts(){
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


    @AfterEach
    public void tearDown() {
        accountService = null;
    }

}