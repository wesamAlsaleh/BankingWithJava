package Currency;

import Global.Utils.Printer;

import java.util.List;

public class CurrencyService {
    private final Printer printer = new Printer();
    private final CurrencyRepository currencyRepository = new CurrencyRepository();

    // function to convert from sender to receiver currency
    public double convertCurrency(String senderCurrency, String receiverCurrency, double amount) {
        // get the currencies
        var currencies = currencyRepository.getCurrencies();

        // if no currencies print error
        if (currencies.isEmpty()) {
            printer.printError("No currencies found!");
            return 0; // failer
        }

        // availability flags
        var senderFlag = false;
        var receiverFlag = false;
        var senderExchangeRate = 0d;
        var receiverExchangeRate = 0d;

        // check both currencies in the system
        for (var currency : currencies) {
            if (currency.currencyCode().equals(senderCurrency)) {
                senderFlag = true;
                senderExchangeRate = currency.exchangeRate();
            }

            if (currency.currencyCode().equals(receiverCurrency)) {
                receiverFlag = true;
                receiverExchangeRate = currency.exchangeRate();
            }
        }

        // if one of the currencies is not available return failer
        if (!senderFlag || !receiverFlag) {
            printer.printError(String.format("Cannot transfer money from %s to %s accounts due to currency rate issues.", senderCurrency, receiverCurrency));
            return 0;
        }

        // get the amount in USD
        var usd = amount * senderExchangeRate; // senderExchangeRate muse relate to USD!

        System.out.println("USD: " + usd);
        System.out.println("receiver: " + usd / receiverExchangeRate);

        // get the amount in the receiver currency
        return usd / receiverExchangeRate;
    }

    // function to add currency to the system
    public boolean addCurrency(String country, String currencyCode, double exchangeRate) {
        // create currency object
        var currency = new Currency(country, currencyCode, exchangeRate);

        // if the currency is not available in the system
        if (!currencyRepository.isCurrencyAvailable(currency.createCurrencyRecord())) {
            // add it to the system
            currencyRepository.writeCurrencyRecord(currency.createCurrencyRecord());

            // indicate the operation is successful
            return true;
        }

        // return already exits in the system
        printer.printError("This currency already exists!");

        // indicate the operation is failed
        return false;
    }

    // function to get the currencies in the system
    public void printCurrencies() {
        // get the currencies list
        var currencies = currencyRepository.getCurrencies();

        // if no currencies print message
        if (currencies.isEmpty()) {
            printer.printColoredLine(Printer.YELLOW, "No currencies available in the system!");
            System.out.println(" "); // space below
        }

        // iterate over them
        for (var currency : currencies) {
            // format the output
            var f = String.format("Country: %s -- Currency Code: %s -- Rate: %f%%",
                    currency.country(),
                    currency.currencyCode(),
                    currency.exchangeRate()
            );

            // print the formated line
            System.out.println(f);
            System.out.println(" "); // space below each currency line
        }
    }

    // function to get currencies list
    public List<Currency> getCurrencies() {
        return currencyRepository.getCurrencies();
    }

    // function to check if the input code is in the list
    public boolean currencyExistsInTheSystem(String currencyCode) {
        // get the currencies array
        var currencies = getCurrencies();

        // iterate over the currencies
        for (var currency : currencies) {
            // if the selected currency code is in the system return true
            if (currency.currencyCode().equals(currencyCode)) {
                return true;
            }
        }
        // return false
        return false;
    }

    // function to delete currency by currency code
    public void deleteCurrency(String currencyCode) {
        // if the currency is not in the system return error
        if (!currencyRepository.isCurrencyAvailable(currencyCode)) {
            printer.printError("This currency does not exist!");
            return; // do not delete the currency
        }
        // delete the currency
        currencyRepository.deleteCurrency(currencyCode);
    }
}
