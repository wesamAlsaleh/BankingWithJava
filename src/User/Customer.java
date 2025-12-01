package User;

import Account.Account;
import DebitCard.DebitCard;

import java.util.List;

public class Customer extends User {

    private Long userId;
    private List<Account> accounts; // user accounts
    private List<DebitCard> debitCards; // user debit card

    // default constructor
    public Customer(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

}
