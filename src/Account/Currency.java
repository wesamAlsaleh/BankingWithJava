package Account;

public class Currency {
    private final String currency;
    private final double exchangeRate;

    // default constructor (used to do the setters)
    public Currency(String currency, double exchangeRate) {
        this.currency = currency;
        this.exchangeRate = exchangeRate;
    }

    // getters
    public String getCurrency() {
        return currency;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }
}
