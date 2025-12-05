package Account;

import Global.Utils.Printer;
import Global.Utils.DBPaths;
import Global.Utils.FileHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountRepository {
    private final DBPaths dbPaths = new DBPaths();
    private final FileHandler fileHandler = new FileHandler();
    private final Printer printer = new Printer();

    // function to extract record to account object
//    private Account recordToAccount(String recordLine) {}

    // function to write a new record in the account number list! file
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

    // function to check if the account number is in the list
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

    // function to write a new account record in the accounts directory
    public void saveNewAccountRecord(String fileName, String accountRecord) {
        // get the path of the parent directory
        var rootDirectory = dbPaths.getAccountsDirectoryPath();

        // prepare the path of the new file
        var newFile = new File(rootDirectory, fileName); // this will handle the path

        // create new file
        fileHandler.createFile(
                newFile,
                "Failed to create account record file!"
        );

        // write in the file
        fileHandler.write(
                newFile.getPath(),
                accountRecord,
                "Failed to write account account record!"
        );
    }

    // function to check if the user has an account
    public boolean userHasAnAccountRecord(Long userId) {
        // get the directory files
        var files = fileHandler.getDirectoryContentAsList(dbPaths.getAccountsDirectoryPath());

        // iterate the files
        for (File file : files) {
            // if the user has any record that contain his id
            if (file.getName().endsWith("-" + userId + ".txt")) return true;
        }

        // if not available return false
        return false;
    }

    // function to get the accounts as files
    public List<Account> getAllAccountsByUserId(Long userId) {
        // get the files
        var files = fileHandler.getDirectoryContentAsList(dbPaths.getAccountsDirectoryPath());

        // array holder
        List<Account> accounts = new ArrayList<>();

        // iterate
        for (File file : files) {
            // if the file has the user id
            if (file.getName().endsWith("-" + userId + ".txt")) {
                // try to read the file
                try (Scanner scanner = new Scanner(new File(file.getPath()))) {
                    // while there is a line
                    while (scanner.hasNextLine()) {
                        // get the record line
                        String line = scanner.nextLine();

                        // get the record as parts
                        var parts = new ArrayList<>(List.of(line.split(",")));

                        // extract the values
                        var id = parts.get(0).split(":")[1];
                        var accountNumber = parts.get(1).split(":")[1];
                        var iban = parts.get(2).split(":")[1];
                        var accountType = parts.get(3).split(":")[1];
                        var accountCurrency = parts.get(4).split(":")[1];
                        var balance = parts.get(5).split(":")[1];
                        var overdraftCount = parts.get(6).split(":")[1];
                        var isActive = parts.get(7).split(":")[1];
                        var isMainAccount = parts.get(8).split(":")[1];
                        var createdAt = parts.get(9).substring(parts.get(9).indexOf(":") + 1);

                        // create account object
                        var account = new Account(
                                Long.valueOf(id),
                                accountNumber,
                                iban,
                                AccountType.valueOf(accountType),
                                accountCurrency,
                                Double.parseDouble(balance),
                                Integer.parseInt(overdraftCount),
                                Boolean.getBoolean(isActive),
                                Boolean.getBoolean(isMainAccount),
                                LocalDateTime.parse(createdAt)
                        );

                        // add the account to the list
                        accounts.add(account);
                    } // while end
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } // catch end
            } // if end
        } // for loop end

        // return the array
        return accounts;
    }

}
