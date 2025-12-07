package Transaction;

import Global.Utils.DBPaths;
import Global.Utils.FileHandler;

import java.io.File;

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
}
