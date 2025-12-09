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



}