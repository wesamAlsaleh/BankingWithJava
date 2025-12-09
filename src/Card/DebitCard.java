package Card;

import Transaction.TransactionType;

import java.util.Map;

public class DebitCard {
    private Long userId;
    private String accountNumber;
    private String cardNumber;
    private DebitCardType type;

    // default constructor
    public DebitCard(Long userId, String accountNumber, String cardNumber, DebitCardType type) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.cardNumber = cardNumber;
        this.type = type;
    }

    // function to create a debit card record
    public String createDebitCardRecord() {
        return String.format("User_Id:%s, Account_Number:%s, Card_Number:%s, Card_Type:%s",
                userId,
                accountNumber,
                cardNumber,
                type
        );
    }

    // card limit configuration [[MASTERCARD:[[WITHDRAW:5k, TRANSFER:10000]...]], [TITANIUM:[[],[]...]], [PLATINUM:[[],[]...]]]
    private static final Map<DebitCardType, Map<TransactionType, Double>> CARD_LIMITS = Map.of(
            DebitCardType.MASTERCARD, Map.of(
                    TransactionType.WITHDRAW, 5000.0,
                    TransactionType.TRANSFER, 10000.0,
                    TransactionType.TRANSFER_OWN, 20000.0,
                    TransactionType.DEPOSIT, 100000.0,
                    TransactionType.DEPOSIT_OWN, 200000.0
            ),
            DebitCardType.TITANIUM, Map.of(
                    TransactionType.WITHDRAW, 10000.0,
                    TransactionType.TRANSFER, 20000.0,
                    TransactionType.TRANSFER_OWN, 40000.0,
                    TransactionType.DEPOSIT, 100000.0,
                    TransactionType.DEPOSIT_OWN, 200000.0
            ),
            DebitCardType.PLATINUM, Map.of(
                    TransactionType.WITHDRAW, 20000.0,
                    TransactionType.TRANSFER, 40000.0,
                    TransactionType.TRANSFER_OWN, 80000.0,
                    TransactionType.DEPOSIT, 100000.0,
                    TransactionType.DEPOSIT_OWN, 200000.0
            )
    );

    // function to get the card limit
    public double getLimit(TransactionType operationType) {
        return CARD_LIMITS.get(this.type).get(operationType);
    }

    // getters
    public Long getUserId() {
        return userId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public DebitCardType getType() {
        return type;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String toString() {
        return "DebitCard{" +
                "userId=" + userId +
                ", accountNumber='" + accountNumber + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", type=" + type +
                '}';
    }
}
