package Currency;

public class Currency {
    private final String currencyCode;
    private final double exchangeRate;


    // default constructor
    public Currency(String currencyCode, double exchangeRate) {
        this.currencyCode = currencyCode;
        this.exchangeRate = exchangeRate;
    }

    // getters
    public String getCurrencyCode() {
        return currencyCode;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    // function to write a currency record
    public String currencyRecord() {
        return String.format("Currency_Code:%s, Exchange_Rate:%.2f", currencyCode, exchangeRate); // %.2f => .00
    }
}
