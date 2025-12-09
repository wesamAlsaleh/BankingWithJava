package Card;

import Global.Utils.DBPaths;
import Global.Utils.FileHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DebitCardRepository {
    private final DBPaths dbPaths = new DBPaths();
    private final FileHandler fileHandler = new FileHandler();

    // check if the card number does exits in the system
    public boolean isCardNumberUnique(String cardNumber) {
        // read the file that contain the numbers list
        try (Scanner scanner = new Scanner(new File(dbPaths.getCardNumbersFilePath()))) {
            // while data
            while (scanner.hasNextLine()) {
                // read the record line
                if (scanner.nextLine().contains(cardNumber)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Card Numbers File Not Found!");
        }

        return false; // default
    }

    // function to store card number in the file list
    public void writeCardNumber(String cardNumber) {
        fileHandler.writeWithoutAppending(
                dbPaths.getCardNumbersFilePath(),
                cardNumber + "\n", // each in one line
                "Failed to write card number!"
        );
    }

    // function to store card record in the system
    public boolean saveCard(DebitCard debitCard) {
        // prepare the file
        var fileName = debitCard.getCardNumber() + "-" + debitCard.getUserId() + ".txt";
        var file = new File(dbPaths.getCardsDirectoryPath(), fileName);

        try {
            // write the record
            fileHandler.write(
                    file.getPath(),
                    debitCard.createDebitCardRecord(),
                    "Failed to save card record!"
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // function to extract the data from the transaction record
    private DebitCard extractCardDataFromRecord(String record) {
        // convert the record into part
        var parts = new ArrayList<>(List.of(record.split(",")));

        // extract the values
        var userId = parts.get(0).split(":")[1];
        var accountNumber = parts.get(1).split(":")[1];
        var cardNumber = parts.get(2).split(":")[1];
        var cardType = parts.get(3).split(":")[1];

        // create obj
        return new DebitCard(
                Long.valueOf(userId),
                accountNumber,
                cardNumber,
                DebitCardType.valueOf(cardType)
        );
    }

    // function to extract card from record
    private DebitCard readCard(File recordFile) {
        // try to read the file
        try (Scanner scanner = new Scanner(recordFile)) {
            // while data
            while (scanner.hasNextLine()) {
                // get the record
                String line = scanner.nextLine();

                // return the object
                return extractCardDataFromRecord(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Transaction file not found!");
        }

        // should not reach here!
        return null;
    }

    // function to get user cards
    public List<DebitCard> getUserCards(Long userId) {
        // get all the cards in the system
        var files = fileHandler.getDirectoryContentAsList(dbPaths.getCardsDirectoryPath());

        // prepare the array
        List<DebitCard> userCards = new ArrayList<>();

        // iterate over the directory files
        for (File file : files) {
            // if the file contain the user id get it
            if (file.getPath().endsWith("-" + userId + ".txt")) {
                // convert it to obj and add it
                userCards.add(readCard(file));
            }
        }

        // return the list
        return userCards;
    }

    // function to get the debit card by card number
    public DebitCard getDebitCardByCardNumber(String cardNumber) {
        // get the system debit cards
        var files = fileHandler.getDirectoryContentAsList(dbPaths.getCardsDirectoryPath());

        // iterate over them
        for (File file : files) {
            if (file.getName().startsWith(cardNumber + "-")) {
                return readCard(file);
            }
        }

        // not found
        return null;
    }

}
