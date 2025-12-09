package Transaction;

import Account.AccountService;
import Card.DebitCardService;
import User.User;
import User.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DebitCardTransactionServiceTest {

    DebitCardTransactionService debitCardTransactionService;
    AccountService accountService;
    DebitCardService debitCardService;
    User user;


    @BeforeEach
    void setUp() {
        debitCardTransactionService = new DebitCardTransactionService();
        accountService = new AccountService();
        debitCardService = new DebitCardService();
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
    @DisplayName("Should deposit money to account by debit card")
    void shouldDepositMoneyToAccountByDebitCard() {
        // Arrange
        var accountToDeposit = accountService.getUserAccounts(user).get(0);
        var userCard = debitCardService.getUserCards(accountToDeposit.getUserId()).get(0);
        var amountToDeposit = 10.0; // 10BHD -> 26.53USD


        // Act
        debitCardTransactionService.depositMoney(userCard.getCardNumber(), amountToDeposit);
    }

}