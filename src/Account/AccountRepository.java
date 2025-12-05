package Account;

import Global.Utils.Printer;
import Global.Utils.DBPaths;
import Global.Utils.FileHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AccountRepository {
    private final DBPaths dbPaths = new DBPaths();
    private final Printer printer = new Printer();
    private final FileHandler fileHandler = new FileHandler();

    // function to read the account number list file and return it as a file
    public void readAccountsListFile(String accountNumber) {

    }

    // function to write a new record in the account number list file
    public void saveAccountsListFile(String accountNumber) {
        // add new record on new line
        var contentToAppend = "\n" + accountNumber;

        // try to write the account number in the file (append to true to not overwrite the number)
        fileHandler.write(
                dbPaths.getAccountNumberListPath(),
                contentToAppend,
                "Error while writing accounts list file"
        );
    }

    // function to check if a number is in the list
    public boolean existInAccountsListFile(String accountNumber) {
        // try to read the file
        try (Scanner scanner = new Scanner(new File(dbPaths.getAccountNumberListPath()))) {
            // while there is a line read
            while (scanner.hasNextLine()) {
                // get the line
                String line = scanner.nextLine();

                // if the number exist return true
                if (line.contains(accountNumber)) return true;
            }

            // if account not found return false
            return false;
        } catch (FileNotFoundException e) {
            printer.printError("Error while reading accounts list file");
            throw new RuntimeException(e);
        }
    }

    // function to read a record from accounts directory
    public void readAccountRecordFile() {
    }

    // function to write a new account record in the accounts directory
    public void saveNewAccountRecord() {
    }

    // function to check if the user has an account
}
