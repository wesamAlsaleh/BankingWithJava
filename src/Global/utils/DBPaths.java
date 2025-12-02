package Global.utils;

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
    private String pathBasedOnOs(String winLink, String macLink, String linuxLink) {
        if (getOS().equals("windows")) {
            return winLink;
        } else if (getOS().equals("linux")) {
            return linuxLink;
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
                "/Users/wesammuneer/IdeaProjects/BankingWithJava/db/user_index.txt",
                ""
        );
    }

    // function to get the path of the directory that contains the users files
    public String getUsersPath() {
        // return the path of the users directory based on the OS
        return pathBasedOnOs(
                "C:\\Users\\wesam\\Desktop\\GA\\BankingWithJava\\db\\users",
                "/Users/wesammuneer/IdeaProjects/BankingWithJava/db/users",
                ""
        );
    }

}
