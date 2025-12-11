package Global.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DBPaths {

    // define the db root path
    private static final Path DB_ROOT = Paths.get("db");

    // function to get the operating system
    private static String getOS() {
        // get the OS
        var os = System.getProperty("os.name");

        if (os.contains("Windows")) {
            return "windows";
        } else if (os.contains("Linux")) {
            return "linux";
        } else if (os.contains("Mac")) {
            return "mac";
        }

        return "unknown";
    }

    // function to map the links based on the OS
    private String pathBasedOnOs(String winLink, String macLink) {
        if (getOS().equals("windows")) {
            return winLink;
        } else if (getOS().equals("linux")) {
            return "";
        } else if (getOS().equals("mac")) {
            return macLink;
        }

        // should not reach here!
        return "unknown";
    }

    // function to get the path of the file that handle the id counter "user_index.txt"
    public String getUserIndexPath() {
        return DB_ROOT.resolve("User_Index.txt").toString();
    }

    // function to get the path of the directory that contains the users files
    public String getUsersDirectoryPath() {
        return DB_ROOT.resolve("users").toString();
    }

    // function to get the path of the directory that contains the customers account files
    public String getAccountsDirectoryPath() {
        return DB_ROOT.resolve("accounts").toString();
    }

    // function to get the path of the file that store all the account numbers
    public String getAccountNumberListPath() {
        return DB_ROOT.resolve("account_number_list.txt").toString();
    }

    // function to get the path of the file that store the currencies list
    public String getCurrenciesListPath() {
        return DB_ROOT.resolve("currencies.txt").toString();

    }

    // function to get the path of the file that store the transactions of the system
    public String getSystemTransactionsFilePath() {
        return DB_ROOT.resolve("systemTransactions.txt").toString();
    }

    // function to get the path of the transactions directory
    public String getTransactionsDirectoryPath() {
        return DB_ROOT.resolve("transactions").toString();
    }

    // function to get the card numbers list
    public String getCardNumbersFilePath() {
        return DB_ROOT.resolve("card_numbers_list.txt").toString();
    }

    // function to get the path of the cards directory
    public String getCardsDirectoryPath() {
        return DB_ROOT.resolve("cards").toString();
    }

    // function to get the path of the debit cards transactions
    public String getDebitCardTransactionsDirectoryPath() {
        return DB_ROOT.resolve("debit_card_transactions").toString();
    }
}


