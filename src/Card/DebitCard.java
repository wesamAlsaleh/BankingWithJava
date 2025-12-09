package Card;

import Transaction.TransactionType;

import java.util.Map;

public class DebitCard {
    private Long userId;
    private String accountNumber;
    private String cardNumber;
    private DebitCardType type;
    private double cashBackRate;

    // default constructor
    public DebitCard(Long userId, String accountNumber, String cardNumber, DebitCardType type, double cashBackRate) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.cardNumber = cardNumber;
        this.type = type;
        this.cashBackRate = cashBackRate;
    }

    // function to create a debit card record
    public String createDebitCardRecord() {
        return String.format("User_Id:%s, Account_Number:%s, Card_Number:%s, Card_Type:%s, Card_Cash_Back_Rate:%s",
                userId,
                accountNumber,
                cardNumber,
                type,
                cashBackRate
        );
    }

    // card limit configuration [[MASTERCARD:[[WITHDRAW:5k, TRANSFER:10000]...]], [TITANIUM:[[],[]...]], [PLATINUM:[[],[]...]]]
    private static final Map<DebitCardType, Map<CardOperationType, Integer>> CARD_LIMITS = Map.of(
            DebitCardType.MASTERCARD, Map.of(
                    CardOperationType.WITHDRAW, 5000,
                    CardOperationType.TRANSFER, 10000,
                    CardOperationType.TRANSFER_OWN, 20000,
                    CardOperationType.DEPOSIT, 100000,
                    CardOperationType.DEPOSIT_OWN, 200000
            ),
            DebitCardType.TITANIUM, Map.of(
                    CardOperationType.WITHDRAW, 10000,
                    CardOperationType.TRANSFER, 20000,
                    CardOperationType.TRANSFER_OWN, 40000,
                    CardOperationType.DEPOSIT, 100000,
                    CardOperationType.DEPOSIT_OWN, 200000
            ),
            DebitCardType.PLATINUM, Map.of(
                    CardOperationType.WITHDRAW, 20000,
                    CardOperationType.TRANSFER, 40000,
                    CardOperationType.TRANSFER_OWN, 80000,
                    CardOperationType.DEPOSIT, 100000,
                    CardOperationType.DEPOSIT_OWN, 200000
            )
    );

    // function to get the card limit
    public int getLimit(CardOperationType operationType) {
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

    public double getCashBackRate() {
        return cashBackRate;
    }
}
