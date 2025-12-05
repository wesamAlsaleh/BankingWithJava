package Currency;

import Global.Utils.DBPaths;
import Global.Utils.Printer;

import java.io.File;

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
}
