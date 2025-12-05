package Currency;

import Global.Utils.Printer;

import java.util.List;

public class CurrencyService {
    private final Printer printer = new Printer();
    private final CurrencyRepository currencyRepository = new CurrencyRepository();

    // function to add currency to the system
    public boolean addCurrency(String currencyCode, double exchangeRate) {
        // create currency object
        var currency = new Currency(currencyCode, exchangeRate);

        // if the currency is not available in the system
        if (!currencyRepository.isCurrencyAvailable(currency.currencyRecord())) {
            // add it to the system
            currencyRepository.writeCurrencyRecord(currency.currencyRecord());

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

        // iterate over them
        for (var currency : currencies) {
            // format the output
            var c = String.format("Currency Code: %s -- Rate: %.2f", currency.getCurrencyCode(), currency.getExchangeRate());

            // print the formated line
            System.out.println(c);
        }
    }

    // function to get currencies list
    public List<Currency> getCurrencies() {
        return currencyRepository.getCurrencies();
    }

    // function to check if the input code is in the list
    public boolean isCurrencyVerified(String currencyCode) {
        // get the currencies array
        var currencies = getCurrencies();

        // iterate over the currencies
        for (var currency : currencies) {
            // if the selected currency code is in the system return true
            if (currency.getCurrencyCode().equals(currencyCode)) {
                return true;
            }
        }
        // return false
        return false;
    }
}
