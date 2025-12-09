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

    @Test
    @DisplayName("Should print user debit cards")
    void printUserDebitCards() {
        // Arrange

        // Act
        debitCardService.printUserCards(user.getId());
    }

    @Test
    @DisplayName("Should return user card by card number")
    void getUserCardByCardNumber() {
        // Arrange
        var cardNumber = debitCardService.getUserCards(user.getId()).get(0).getCardNumber();

        // Act
        var userCard = debitCardService.getDebitCardByCardNumber(cardNumber);

        // Assert
        assertNotNull(userCard);
    }

    @Test
    @DisplayName("Should deposit money to account by debit card")
    void shouldDepositMoneyToAccountByDebitCard() {
        // Arrange
        var accountToDeposit = accountService.getUserAccounts(user).get(0);
        var userCard = debitCardService.getUserCards(accountToDeposit.getUserId()).get(0);
        var amountToDeposit = 10.0; // 10BHD -> 26.53USD


        // Act
        debitCardService.depositMoney(userCard.getCardNumber(), amountToDeposit);
    }

    @Test
    @DisplayName("Should withdraw money from an account by debit card")
    void shouldWithdrawMoneyFromAccountByDebitCard() {
        // Arrange
        var accountToWithdraw = accountService.getUserAccounts(user).get(0);
        var userCard = debitCardService.getUserCards(accountToWithdraw.getUserId()).get(0);
        var amountToWithdraw = 10.0; // 10BHD -> 26.53USD


        // Act
        debitCardService.withdrawMoney(userCard.getCardNumber(), amountToWithdraw);
    }

    @AfterEach
    void tearDown() {
        debitCardService = null;
    }

}