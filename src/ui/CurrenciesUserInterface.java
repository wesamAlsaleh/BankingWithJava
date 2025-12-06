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

    // function to validate the country
    private String validateCountry(String country) {
        // if the name is empty
        if (country.isBlank()) {
            return "Country is required!";
        }

        // if the country is less than 4 characters return error
        if (country.length() < 4) {
            return "Country must be at least 4 characters!";
        }

        return "";
    }

    // function to validate currency code input
    private String validateCurrencyCode(String currencyCode) {
        // validate the code
        if (currencyCode == null || currencyCode.length() != 3)
            return "Please enter a valid currency code!";

        // return empty string
        return "";
    }

    // function to validate currency exchange rate input
    private String validateExchangeRate(double exchangeRate) {
        // validate the code
        if (exchangeRate <= 0) return "Please enter a valid exchange rate!";

        // return empty string
        return "";
    }

    // function to validate the input for create new currency record
    private String validateCreateCurrencyInput(String country, String currencyCode, double exchangeRate) {
        // messages holder
        StringBuilder stringBuilder = new StringBuilder();

        // if there are any safety violation
        stringBuilder.append(validateCountry(country));
        stringBuilder.append(validateCurrencyCode(currencyCode));
        stringBuilder.append(validateExchangeRate(exchangeRate));

        // return the messages
        return stringBuilder.toString();
    }

    // function to validate the input for deleting currency record
    private String validateDeleteCurrencyInput(String currencyCode) {
        return validateCurrencyCode(currencyCode);
    }

    // function to show the add currencies page
    private void addCurrencyPage() {
        // init message
        printer.printColoredTitle("Add new currency");

        while (true) {
            try {
                // country name input
                printer.printQuestion("What is the country:");
                String country = scanner.nextLine();

                // currency code input
                printer.printQuestion("What is the currency code:");
                String currencyCode = scanner.nextLine();

                // exchange rate input as double!
                printer.printQuestion("What is the exchange rate of:");
                double exchangeRate = scanner.nextDouble();
                scanner.nextLine(); // consume newline after nextDouble() !

                // validate the input
                var reply = validateCreateCurrencyInput(country, currencyCode, exchangeRate);

                // if no error proceed the operation
                if (reply.isEmpty()) {
                    // add the currency to the system
                    var success = currencyService.addCurrency(country, currencyCode, exchangeRate);

                    // if the operation failed reset the loop
                    if (!success) continue; // restart the loop

                    // exit the while loop
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

    // function to show delete currencies page
    public void deleteCurrenciesPage() {
        // init message
        printer.printColoredTitle("Delete currencies");

        // print the all the currencies in the system to help the user
        currencyService.printCurrencies();

        while (true) {
            // if no currencies found
            if (currencyService.getCurrencies().isEmpty()) {
                break; // exit the while loop
            }

            printer.printQuestion("What is the currency code:");
            String currencyCode = scanner.nextLine();

            // validate the input
            var reply = validateDeleteCurrencyInput(currencyCode);

            // if no errors
            if (reply.isEmpty()) {
                // delete the currency from the system
                currencyService.deleteCurrency(currencyCode);
            }

            // exit the while loop to return the parent page
            break;
        }
        // automatically redirected
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
                    deleteCurrenciesPage();
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
