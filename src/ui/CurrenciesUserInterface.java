package ui;

import Currency.CurrencyService;
import Global.Utils.Printer;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CurrenciesUserInterface {
    private final Scanner scanner = new Scanner(System.in);
    private final Printer printer = new Printer();
    //    private final StartUpUserInterface startUpUserInterface = new StartUpUserInterface();
    private final CurrencyService currencyService = new CurrencyService();

    // function to validate the input
    private String validateCreateCurrencyInput(String currencyCode, double exchangeRate) {
        // messages holder
        StringBuilder stringBuilder = new StringBuilder();

        // if any of the input return message
        if (currencyCode == null || currencyCode.isEmpty()) stringBuilder.append("Please enter a currency code!");
        if (exchangeRate <= 0) stringBuilder.append("Please enter a valid exchange rate!");

        // if the currency code is greater than 3 return error

        // return the messages
        return stringBuilder.toString();
    }

    // function to show the add currencies page
    private void addCurrencyPage() {
        // init message
        printer.printColoredTitle("Add new currency");

        while (true) {
            try {
                printer.printQuestion("What is the currency code:");
                String currencyCode = scanner.nextLine();

                printer.printQuestion("What is the exchange rate of:");
                double exchangeRate = scanner.nextDouble();
                scanner.nextLine(); // consume newline after nextDouble() !

                // validate the input
                var reply = validateCreateCurrencyInput(currencyCode, exchangeRate);

                // if no error proceed the operation
                if (reply.isEmpty()) {
                    // add the currency to the system
                    var success = currencyService.addCurrency(currencyCode, exchangeRate);

                    // if the operation failed reset the loop
                    if (!success) continue; // restart the loop

                    // exit the loop
                    break;
                }
            } catch (InputMismatchException e) {
                printer.printError("Please enter a valid code and exchange rate!");
                scanner.nextLine(); // flush the buffer
            }
        }
    }

    // function to show see all currencies page
    private void seeAllCurrenciesPage() {
        // init message
        printer.printColoredTitle("See all currencies");

        // print the all the currencies in the system
        currencyService.printCurrencies();
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
            System.out.println("[q] Quit / Logout");
            printer.printPrompt("Your choice: ");

            // input from the user
            var choice = scanner.nextLine().toLowerCase().trim();

            // based on the choice go to the next direction
            switch (choice) {
                case ("s"):
                    seeAllCurrenciesPage();
                    break;
                case ("a"):
                    addCurrencyPage();
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
