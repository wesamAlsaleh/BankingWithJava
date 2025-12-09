package Transaction;

import Card.DebitCard;
import Global.Utils.DBPaths;
import Global.Utils.FileHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DebitCardTransactionRepository {
    private final DBPaths dbPaths = new DBPaths();
    private final FileHandler fileHandler = new FileHandler();

    // function to extract the data from the transaction record
    private DebitCardTransaction extractDataFromRecord(String record) {
        // convert the record into part
        var parts = new ArrayList<>(List.of(record.split(",")));

        // sample line
//        "UUID:%s, User_Id:%s, Account_Number:%s, Card_Number:%s, Transaction_Type:%s, Transaction_Amount_USD:%.3f, Transaction_Date:%s"

        // extract the values
        var uuid = parts.get(0).split(":")[1].trim();
        var userId = parts.get(1).split(":")[1].trim();
        var accountNumber = parts.get(2).split(":")[1];
        var cardNumber = parts.get(3).split(":")[1];
        var transactionType = parts.get(4).split(":")[1];
        var amountInUsd = parts.get(5).split(":")[1];
        var transactionTime = parts.get(6).substring(parts.get(6).indexOf(":") + 1);

        // return the obj
        return new DebitCardTransaction(
                uuid,
                Long.valueOf(userId),
                accountNumber,
                cardNumber,
                TransactionType.valueOf(transactionType),
                Double.parseDouble(amountInUsd),
                LocalDateTime.parse(transactionTime)
        );
    }

    // function to create a new transaction record
    public void saveNewDebitCardTransactionRecord(DebitCardTransaction debitCardTransaction) {
        // prepare the file
        var roodDirectory = dbPaths.getDebitCardTransactionsDirectoryPath();
        var fileName = debitCardTransaction.getId() + "-" + debitCardTransaction.getUserId() + ".txt";
        var file = new File(roodDirectory, fileName);

        // create a file in the transactions directory
        fileHandler.createFile(
                file,
                "Failed to create debit card transaction file!"
        );

        // write the data in the created file
        fileHandler.write(
                file.getPath(),
                debitCardTransaction.createTransactionRecord(),
                "Failed to write debit card transaction file!"
        );
    }

    // function to extract transaction record into object
    public DebitCardTransaction getDebitCardTransaction(File recordFile) {
        // try to read the file
        try (Scanner scanner = new Scanner(recordFile)) {
            // while data
            while (scanner.hasNextLine()) {
                // get the record
                String line = scanner.nextLine();

                // return the object
                return extractDataFromRecord(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Transaction file not found!");
        }

        // should not reach here!
        return null;
    }

    // function to get user transactions
    public List<DebitCardTransaction> getUserDebitCardTransactions(Long userId) {
        // get the files
        var files = fileHandler.getDirectoryContentAsList(dbPaths.getDebitCardTransactionsDirectoryPath());

        // array
        List<DebitCardTransaction> transactions = new ArrayList<>();

        // iterate over them
        for (File file : files) {
            // if the file contain user id add it
            if (file.getName().endsWith("-" + userId + ".txt")) {
                // convert it to obj
                var transaction = getDebitCardTransaction(file);

                // add it to the array
                transactions.add(transaction);
            }
        }

        // return the array
        return transactions;
    }

    // function to get the total sum of the debit card
    public double debitCardTodayTransactionsSum(DebitCard card) {
        // get the files
        var files = fileHandler.getDirectoryContentAsList(dbPaths.getDebitCardTransactionsDirectoryPath());

        // array holder
        List<DebitCardTransaction> transactions = new ArrayList<>();

        // counter holder
        double counter = 0;

        // iterate over
        for (File file : files) {
            // read the file
            try (Scanner scanner = new Scanner(file)) {
                // while data
                while (scanner.hasNextLine()) {
                    // get the line
                    String line = scanner.nextLine();

                    // get the transaction as object
                    var transaction = extractDataFromRecord(line);

                    // if the transaction date is today
                    if (transaction.getTransactionDate().isAfter(LocalDateTime.now().toLocalDate().atStartOfDay())) {
                        // add the transaction
                        transactions.add(transaction);
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Transaction file not found!");

            }
        }

        // calculate the amount spent today
        for (DebitCardTransaction transaction : transactions) {
            counter += transaction.getAmountInUsd();
        }

        // return the number
        return counter;
    }
}
