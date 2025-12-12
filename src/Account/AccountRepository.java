package Account;

import Global.Utils.Printer;
import Global.Utils.DBPaths;
import Global.Utils.FileHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

public class AccountRepository {
    private final DBPaths dbPaths = new DBPaths();
    private final FileHandler fileHandler = new FileHandler();
    private final Printer printer = new Printer();

    // function to extract record to account object
    private Account recordToAccount(String recordLine) {
        // get the record as parts
        var parts = new ArrayList<>(List.of(recordLine.split(",")));

        // extract the values
        var id = parts.get(0).split(":")[1];
        var accountName = parts.get(1).split(":")[1];
        var accountNumber = parts.get(2).split(":")[1];
        var iban = parts.get(3).split(":")[1];
        var accountType = parts.get(4).split(":")[1];
        var accountCurrency = parts.get(5).split(":")[1];
        var balance = parts.get(6).split(":")[1];
        var overdraftCount = parts.get(7).split(":")[1];
        var isActive = parts.get(8).split(":")[1];
        var createdAt = parts.get(9).substring(parts.get(9).indexOf(":") + 1);

        // create account object
        return new Account(
                Long.valueOf(id),
                accountNumber,
                iban,
                accountName,
                AccountType.valueOf(accountType),
                accountCurrency,
                Double.parseDouble(balance),
                Integer.parseInt(overdraftCount),
                Boolean.parseBoolean(isActive),
                LocalDateTime.parse(createdAt)
        );
    }

    // function to write a new record in the account number list! file
    public void saveAccountsListFile(String accountNumber) {
        // add new record on new line
        var contentToAppend = accountNumber + "\n";

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
    public boolean saveNewAccountRecord(String fileName, String accountRecord) {
        try {
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

            // success state
            return true;
        } catch (Exception e) {
            printer.printError("Error while creating account record file");
            return false;
        }
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

                        // get the account as obj
                        var account = recordToAccount(line);

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

    // function to delete an account by account number
    public void deleteAccountRecord(Long userId, String accountNumber) {
        // get the account files
        var files = fileHandler.getDirectoryContentAsList(dbPaths.getAccountsDirectoryPath());

        // iterate over them
        for (File file : files) {
            // if the file starts with the provided account number delete it
            if (file.getName().startsWith(accountNumber)) {
                // if the account number does not belong to the user
                if (!file.getName().endsWith("-" + userId + ".txt")) {
                    printer.printError("Invalid account number!");
                    break; // exit the for loop
                }

                // try to read the file
                try (Scanner scanner = new Scanner(file)) {
                    // while there is data
                    while (scanner.hasNextLine()) {
                        // get the record line
                        String line = scanner.nextLine();

                        // split the record into parts
                        var parts = new ArrayList<>(List.of(line.split(",")));

                        // iterate over the parts
                        for (String part : parts) {
                            // get the isActive part
                            if (part.contains("is_active")) {
                                // extract the value
                                var isActive = part.split(":")[1];

                                // if the account is not active return error
                                if (!Boolean.parseBoolean(isActive)) {
                                    printer.printError("Disabled account can not be deleted!");
                                    return; // exit the function
                                }
                            }

                            // get the balance part
                            if (part.contains("balance")) {
                                // extract the value
                                var balance = part.split(":")[1];

                                // if the balance is greater than 1 return error
                                if (Double.parseDouble(balance) > 1.0) {
                                    printer.printError("Account contain balance can not be deleted!");
                                    return; // exit the function
                                }
                            }
                        } // for loop end
                    } // try end
                } catch (FileNotFoundException e) {
                    printer.printError("File not found!");
                    throw new RuntimeException(e);
                } // catch end

                // remove the account number from the list that contains the account numbers
                fileHandler.overwriteList(new File(dbPaths.getAccountNumberListPath()), accountNumber);

                // try to delete it
                if (file.delete()) {
                    // show success message
                    printer.printSuccessful("Account deleted successfully!");
                } else {
                    printer.printError("Error while deleting account record!");
                } // else end
            } // if end
        } // for end
    }

    // function to update account
    public boolean updateAccountRecord(Account account) {
        try {
            // get the file that starts with the account number
            var files = fileHandler.getDirectoryContentAsList(dbPaths.getAccountsDirectoryPath());

            // iterate over them
            for (File file : files) {
                if (file.getName().startsWith(account.getAccountNumber() + "-")) {
                    // validate the user (authorization required)
                    if (!file.getName().endsWith("-" + account.getUserId() + ".txt")) {
                        return false;
                    }

                    // overwrite the file
                    fileHandler.writeWithoutAppending(
                            file.getPath(),
                            account.createAccountRecord(),
                            "Failed to overwrite account record file!"
                    );

                    // return success
                    return true;
                }
            }
        } catch (Exception e) {
            printer.printError("Error while updating account record!");
        }

        // return failer
        return false;
    }

    // function to get the account by account number
    public Account getAccountByAccountNumber(String accountNumber) {
        // get the accounts directory files
        var files = fileHandler.getDirectoryContentAsList(dbPaths.getAccountsDirectoryPath());

        // get the accounts in the system
        for (File file : files) {
            if (file.getName().startsWith(accountNumber + "-")) {
                // read the file
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        return recordToAccount(scanner.nextLine());
                    }
                } catch (FileNotFoundException e) {
                    printer.printError("File not found!");
                    throw new RuntimeException(e);
                }
            }
        }

        // if not found
        return null;
    }
}
