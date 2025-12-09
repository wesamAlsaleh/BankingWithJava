package Card;

import Global.Utils.Printer;

public class DebitCardService {
    private final DebitCardRepository debitCardRepository = new DebitCardRepository();
    private final Printer printer = new Printer();

    // function to generate card number
    private String generateCardNumber() {
        // card number holder
        StringBuilder cardNumber = new StringBuilder();

        // if the number is existed generate again
        do {
            // generate 16 random digits
            for (int i = 0; i < 16; i++) {
                cardNumber.append((int) (Math.random() * 10)); // random 0 to 9
            }

        } while (debitCardRepository.isCardNumberUnique(cardNumber.toString()));

        // save the number in the system
        debitCardRepository.writeCardNumber(cardNumber.toString());

        // return the number
        return cardNumber.toString();
    }

    // function to create a debit card
    public void createDebitCard(Long userId, String accountNumber, DebitCardType type) {
        // generate card number
        var cardNumber = generateCardNumber();

        // get the user cards
        var userCards = debitCardRepository.getUserCards(userId);

        // if the user has a debit card with the same account number return error
        for (DebitCard card : userCards) {
            if (card.getAccountNumber().equals(accountNumber)) {
                printer.printError("Debit Card already exists for this account!");
                return; // do nothing
            }
        }

        // create card instance
        var card = new DebitCard(
                userId,
                accountNumber,
                cardNumber,
                type,
                0.05
        );

        // save the card in the system
        var success = debitCardRepository.saveCard(card);

        // if successfully put message
        if (success) {
            printer.printSuccessful("Card created successfully!");

            // todo: put notification
        } else {
            printer.printError("Failed to create card!");
        }
    }

    // function to print user cards
    public void printUserCards(Long userId) {
        // get the user cards
        var userCards = debitCardRepository.getUserCards(userId);

        // if no cards print error
        if (userCards.isEmpty()) {
            printer.printError("No user cards found!");
            return; // do nothing
        }

        // format the print
        for (DebitCard card : userCards) {
            printer.printColoredLine(Printer.YELLOW, String.format("Card number: %s -- Card type: %s -- Cash back rate: %s for account: %s",
                    card.getCardNumber(),
                    card.getType().toString().toLowerCase(),
                    card.getCashBackRate(),
                    card.getAccountNumber()
            ));
            System.out.println(" "); // line after each
        }
    }
}
