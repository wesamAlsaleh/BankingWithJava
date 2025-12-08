package Global.Utils;

public class DBPaths {
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
        // return the path of file "user_index.txt" based on the OS
        return pathBasedOnOs(
                "C:\\Users\\wesam\\Desktop\\GA\\BankingWithJava\\db\\user_index.txt",
                "/Users/wesammuneer/IdeaProjects/BankingWithJava/db/user_index.txt"
        );
    }

    // function to get the path of the directory that contains the users files
    public String getUsersDirectoryPath() {
        // return the path of the users directory based on the OS
        return pathBasedOnOs(
                "C:\\Users\\wesam\\Desktop\\GA\\BankingWithJava\\db\\users",
                "/Users/wesammuneer/IdeaProjects/BankingWithJava/db/users"
        );
    }

    // function to get the path of the directory that contains the customers account files
    public String getAccountsDirectoryPath() {
        return pathBasedOnOs(
                "C:\\Users\\wesam\\Desktop\\GA\\BankingWithJava\\db\\accounts",
                "/Users/wesammuneer/IdeaProjects/BankingWithJava/db/accounts"
        );
    }

    // function to get the path of the file that store all the account numbers
    public String getAccountNumberListPath() {
        return pathBasedOnOs(
                "C:\\Users\\wesam\\Desktop\\GA\\BankingWithJava\\db\\account_number_list.txt",
                "/Users/wesammuneer/IdeaProjects/BankingWithJava/db/account_number_list.txt"
        );
    }

    // function to get the path of the file that store the currencies list
    public String getCurrenciesListPath() {
        return pathBasedOnOs(
                "C:\\Users\\wesam\\Desktop\\GA\\BankingWithJava\\db\\currencies.txt",
                "/Users/wesammuneer/IdeaProjects/BankingWithJava/db/currencies.txt"
        );
    }

    // function to get the path of the file that store the transactions of the system
    public String getSystemTransactionsFilePath() {
        return pathBasedOnOs(
                "C:\\Users\\wesam\\Desktop\\GA\\BankingWithJava\\db\\systemTransactions.txt",
                "/Users/wesammuneer/IdeaProjects/BankingWithJava/db/systemTransactions.txt"
        );
    }

    // function to get the path of the transactions directory
    public String getTransactionsDirectoryPath() {
        return pathBasedOnOs(
                "C:\\Users\\wesam\\Desktop\\GA\\BankingWithJava\\db\\transactions",
                "/Users/wesammuneer/IdeaProjects/BankingWithJava/db/transactions"
        );
    }
}


