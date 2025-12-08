package Currency;

public record Currency(String country, String currencyCode, double exchangeRate) {
    // function to write a currency record
    public String createCurrencyRecord() {
        return String.format("Country:%s, Currency_Code:%s, Exchange_Rate:%f",
                country,
                currencyCode,
                exchangeRate
        ); // %.2f => .00
    }

    @Override
    public String toString() {
        return "Currency{" +
                "country='" + country + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", exchangeRate=" + exchangeRate +
                '}';
    }
}
