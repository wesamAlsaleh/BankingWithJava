package Currency;

import Global.Utils.DBPaths;
import Global.Utils.FileHandler;

import java.io.File;

public class CurrencyRepository {
    private final FileHandler fileHandler = new FileHandler();
    private final DBPaths dbPaths = new DBPaths();

    // function to check if the currency is available in the system
    public boolean isCurrencyAvailable(String currencyRecord) {
        // get the file that is responsible for the list
        return fileHandler.isExistInTheFile(new File(dbPaths.getCurrenciesListPath()), currencyRecord);
    }

    // function to write currency record in the system
    public void writeCurrencyRecord(String currencyRecord) {
        fileHandler.write(
                new File(dbPaths.getCurrenciesListPath()).getPath(),
                currencyRecord,
                "Failed to add new currency! Please try again."
        );
    }
}
