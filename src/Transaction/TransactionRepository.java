package Transaction;

import Global.Utils.DBPaths;
import Global.Utils.FileHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

public class TransactionRepository {
    private final DBPaths dbPaths = new DBPaths();
    private final FileHandler fileHandler = new FileHandler();

    // function to create a new transaction record
    public void saveNewTransactionRecord(Transaction transaction) {
        // prepare the file
        var roodDirectory = dbPaths.getTransactionsDirectoryPath();
        var fileName = transaction.getId() + "-" + transaction.getUserId() + ".txt";
        var file = new File(roodDirectory, fileName);

        // create a file in the transactions directory
        fileHandler.createFile(
                file,
                "Failed to create transaction file!"
        );

        // write the data in the created file
        fileHandler.write(
                file.getPath(),
                transaction.createTransactionRecord(),
                "Failed to write transaction file!"
        );

        // add the record in the system transactions file
        fileHandler.write(
                dbPaths.getSystemTransactionsFilePath(),
                transaction.createTransactionRecord() + "\n", // each in new line
                "Failed to write system transaction file!"
        );
    }

    // function to extract transaction record into object
    public Transaction getTransaction(File recordFile) {
        // try to read the file
        try (Scanner scanner = new Scanner(recordFile)) {
            // while data
            while (scanner.hasNextLine()) {
                // get the record
                String line = scanner.nextLine();

                // convert the record into part
                var parts = new ArrayList<>(List.of(line.split(",")));

                // extract the values
                var uuid = parts.get(0).split(":")[1].trim();
                var userId = parts.get(1).split(":")[1].trim();
                var accountName = parts.get(2).split(":")[1];
                var accountNumber = parts.get(3).split(":")[1];
                var iban = parts.get(4).split(":")[1];
                var transactionType = parts.get(5).split(":")[1];
                var amount = parts.get(6).split(":")[1];
                var postBalance = parts.get(7).split(":")[1];
                var transactionTime = parts.get(8).substring(parts.get(8).indexOf(":") + 1);

                // return the obj
                return new Transaction(
                        uuid,
                        Long.valueOf(userId),
                        accountName,
                        String.valueOf(accountNumber),
                        iban,
                        TransactionType.valueOf(transactionType),
                        Double.parseDouble(amount),
                        Double.parseDouble(postBalance),
                        LocalDateTime.parse(transactionTime)
                );
            }
        } catch (FileNotFoundException e) {
            System.out.println("Transaction file not found!");
        }

        // should not reach here!
        return null;
    }

    // function to get user transactions
    public List<Transaction> getUserTransactions(Long userId) {
        // get the files
        var files = fileHandler.getDirectoryContentAsList(dbPaths.getTransactionsDirectoryPath());

        // array
        List<Transaction> transactions = new ArrayList<>();

        // iterate over them
        for (File file : files) {
            // if the file contain user id add it
            if (file.getName().endsWith("-" + userId + ".txt")) {
                // convert it to obj
                var transaction = getTransaction(file);

                // add it to the array
                transactions.add(transaction);
            }
        }

        // return the array
        return transactions;
    }


}
