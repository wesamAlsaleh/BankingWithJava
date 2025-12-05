package Account;

import User.User;
import User.UserRepository;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceTest {

    AccountService accountService;

    @BeforeEach
    public void setUp() {
        accountService = new AccountService();
    }

    @Test
    @DisplayName("Should generate unique account number and store it in a file")
    void shouldGenerateUniqueAccountNumberAndStoreItInAFile() {
        // Act
        var accountNumber = accountService.generateAccountNumber();

        System.out.println(accountNumber);

        // Assert
       assertNotNull(accountNumber);
    }

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

    @AfterEach
    public void tearDown() {
        accountService = null;
    }

}