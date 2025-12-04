package Global.Utils;

public class AnsiPrinter {
    // ANSI Colors
    public static final String RESET = "\u001B[0m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";

    // function to print customized one line
    public void printColoredLine(String textColor, String placeholder) {
        System.out.println(textColor + placeholder + RESET);
    }

    // function to print customized one line
    public void printColored(String textColor, String placeholder) {
        System.out.print(textColor + placeholder + RESET);
    }

    // function to print customized title with spaces
    public void printColoredTitle(String textColor, String placeholder) {
        printColoredLine(textColor, "\n===================================");
        System.out.println("\t" + placeholder);
        printColoredLine(textColor, "===================================\n");
    }

    // function to print error message
    public void printError(String placeholder) {
        System.out.println(RED + placeholder + RESET);
    }

    // function to print warning message
    public void printWarning(String placeholder) {
        System.out.println(YELLOW + placeholder + RESET);
    }

    // function to print successful message
    public void printSuccessful(String placeholder) {
        System.out.println(GREEN + placeholder + RESET);
    }

    // function to print cursor line
    public void printPrompt(String placeholder) {
        printColored(GREEN, placeholder);
    }

    // function to print a message of wrong choice
    public void printWrongChoice() {
        printError("Invalid choice. Try again with a valid choice. \n");
    }

    // todo: function to paring important information
}
