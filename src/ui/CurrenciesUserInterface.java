package ui;

import Currency.CurrencyService;
import Global.Utils.Printer;

import java.util.Scanner;

public class CurrenciesUserInterface {
    private final Scanner scanner = new Scanner(System.in);
    private final Printer printer = new Printer();
    //    private final StartUpUserInterface startUpUserInterface = new StartUpUserInterface();
    private final CurrencyService currencyService = new CurrencyService();

    // function to validate the input
    private String validateInput(String currencyCode, double exchangeRate) {
        // messages holder
        StringBuilder stringBuilder = new StringBuilder();

        // if any of the input return message
        if (currencyCode == null || currencyCode.isEmpty()) stringBuilder.append("Please enter a currency code!");
        if (exchangeRate <= 0) stringBuilder.append("Please enter a valid exchange rate!");

        // return the messages
        return stringBuilder.toString();
    }

    // function to show the add currencies page
    private void addCurrencyPage() {
        // init message
        printer.printColoredTitle("Add new currency");

        while (true) {
            System.out.println("What is the currency code:");
            String currencyCode = scanner.nextLine();

            System.out.println("What is the exchange rate of:");
            double exchangeRate = scanner.nextDouble();
            scanner.nextLine(); // ← ADD THIS LINE to consume the newline

            // validate the input
            var reply = validateInput(currencyCode, exchangeRate);

            // if no error proceed the operation
            if (reply.isEmpty()) {
                // add the currency to the system
                var addCurrency = currencyService.addCurrency(currencyCode, exchangeRate);

                // print successful message
                printer.printSuccessful("The currency has been successfully added to the database!");

                // if the operation failed reset the loop
                if (!addCurrency) continue; // restart the loop

                // exit the loop
                break;
            }
        }
    }

    // function to show see all currencies page
    private void seeAllCurrenciesPage() {
        // init message
        printer.printColoredTitle("See all currencies");

        // print the all the currencies in the system
        currencyService.printCurrencies();

        while (true) {
            printer.printColoredLine(Printer.BLUE, "To quit the app press [Y]");

            // get the input
            var choice = scanner.nextLine().trim().toLowerCase();

            // exit the while loop
            if (choice.equals("y")) {
                // terminate the terminal
                System.exit(0); // Exit successfully
                break;
            } else {
                printer.printWrongChoice();
            }
        }
    }

    // function to show the manage currencies page
    public void manageCurrenciesPage() {
        // StartUp Message
        printer.printColoredTitle("Manage currencies");

        // wait for user to chose
        while (true) {
            // options message
            printer.printColoredLine(Printer.BLUE, "Please choose an option:");
            System.out.println("[S] See All Currencies");
            System.out.println("[A] Add currency");
            System.out.println("[D] Delete currency");
            System.out.println("[q]    Quit / Logout");
            printer.printPrompt("Your choice: ");

            // input from the user
            var choice = scanner.nextLine().toLowerCase().trim();

            // based on the choice go to the next direction
            switch (choice) {
                case ("s"):
                    seeAllCurrenciesPage();
                    break;
                case ("a"):
                    addCurrencyPage(); // go to add currencies page
                    break;
                case ("d"):
                    break;
                case ("q"):
                    printer.printSuccessful("Thank you for using GA01 Bank. Goodbye!");
                    System.exit(0); // Exit successfully
                    break;
                default:
                    printer.printWrongChoice();
            }
        }
    }
}
