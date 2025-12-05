package Currency;

import Global.Utils.Printer;

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
            var c = String.format("Currency Code: %s ------ Rate: %f", currency.getCurrencyCode(), currency.getExchangeRate());

            // print the formated line
            System.out.println(c);
        }
    }
}
