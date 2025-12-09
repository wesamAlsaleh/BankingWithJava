package Card;

import Account.AccountService;
import User.User;
import User.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DebitCardServiceTest {

    DebitCardService debitCardService;
    AccountService accountService;
    User user;


    @BeforeEach
    void setUp() {
        debitCardService = new DebitCardService();
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
    @DisplayName("Should create a debit card and store it in the system")
    void createDebitCard() {
        // Arrange
        var userAccount = accountService.getUserAccounts(user).get(0);

        // Act
        debitCardService.createDebitCard(
                user.getId(),
                userAccount.getAccountNumber(),
                DebitCardType.MASTERCARD
        );
    }

    @Test
    @DisplayName("Should return error if the account has already a debit card")
    void createDebitCardWithAlreadyADebitCard() {
        // Arrange
        var userAccount = accountService.getUserAccounts(user).get(0);

        // Act
        debitCardService.createDebitCard(
                user.getId(),
                userAccount.getAccountNumber(),
                DebitCardType.MASTERCARD
        );
    }

    @AfterEach
    void tearDown() {
        debitCardService = null;
    }

}