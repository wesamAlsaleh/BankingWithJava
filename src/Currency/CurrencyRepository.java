package Currency;

import Global.Utils.DBPaths;
import Global.Utils.FileHandler;
import Global.Utils.Printer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CurrencyRepository {
    private final FileHandler fileHandler = new FileHandler();
    private final DBPaths dbPaths = new DBPaths();
    private final Printer printer = new Printer();

    // function to check if the currency is available in the system
    public boolean isCurrencyAvailable(String currencyRecord) {
        // get the file that is responsible for the list
        return fileHandler.isExistInTheFile(new File(dbPaths.getCurrenciesListPath()), currencyRecord);
    }

    // function to write currency record in the system
    public void writeCurrencyRecord(String currencyRecord) {
        fileHandler.write(
                new File(dbPaths.getCurrenciesListPath()).getPath(),
                "\n" + currencyRecord, // for new line
                "Failed to add new currency! Please try again."
        );
    }

    // function to get all the currencies as object from the list file
    public List<Currency> getCurrencies() {
        // currencies array holder
        List<Currency> currencies = new LinkedList<>(); // to Organize the order

        // try to read the currencies
        try (Scanner scanner = new Scanner(new File(dbPaths.getCurrenciesListPath()))) {
            // while there is a line
            while (scanner.hasNextLine()) {
                // get the line
                var line = scanner.nextLine();

                // if the line is empty skip to the next one
                if (line.isEmpty()) continue;

                // split the line
                List<String> parts = new ArrayList<>(List.of(line.split(",")));

                // extract the values
                var currencyCode = parts.get(0).split(":")[1];
                var exchangeRate = parts.get(1).split(":")[1];

                // create instance
                var currency = new Currency(currencyCode, Double.parseDouble(exchangeRate));

                // add it to the array holder
                currencies.add(currency);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Currencies list file not found!");
            System.out.println(e.getMessage());
        }

        // return the list
        return currencies;
    }

    // function to delete a currency by currency code
    public void deleteCurrency(String currencyCode) {
        // get the file that hold the currencies
        var currenciesFile = new File((dbPaths.getCurrenciesListPath()));

        // try to overwrite the currencies list
        fileHandler.overwrite(currenciesFile, "Currency_Code", currencyCode);
    }
}
