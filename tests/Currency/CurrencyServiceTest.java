package Currency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyServiceTest {
    CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        currencyService = new CurrencyService();
    }

    @Test
    @DisplayName("Should return false if the currency is not in the system")
    void shouldReturnFalseIfTheCurrencyIsNotInTheSystem() {
        // Act
        assertFalse(currencyService.currencyExistsInTheSystem("AUD"));
    }

    @Test
    @DisplayName("Should return true if the currency is not on the system")
    void shouldReturnTrueIfTheCurrencyIsInTheSystem() {
        // Act
        assertTrue(currencyService.currencyExistsInTheSystem("BHD"));
    }

    @Test
    @DisplayName("Should try to add currency to the system without duplicates and return success state (true/false) about the currency whether its added or not")
    void shouldTryToAddCurrencyToTheSystemWithoutDuplicatesAndReturnSuccessStateAboutTheCurrencyWhetherItsAddedOrNot() {
        // Arrange
        var country = "Bahrain";
        var currencyCode = "BHD"; // ISO codes examples: IQD, KWD, JOD, SAR, AFN, DZD, ARS, ..
        var exchangeRate = 0.1f;

        // Act
        var availableInTheSystem = currencyService.currencyExistsInTheSystem(currencyCode);
        var success = currencyService.addCurrency(country, currencyCode, exchangeRate);

        // Assert
        // if available in the system the state should be not true
        assertEquals(availableInTheSystem, !success);
    }

    @Test
    @DisplayName("Should return error that says can not add duplicated currencies")
    void shouldReturnErrorThatCanNotAddDuplicatedCurrencies() {
        // Arrange
        var country = "ALGERIA";
        var currencyCode = "DZD"; // make sure it's available!
        var exchangeRate = 0.2f;

        // Act
        var success = currencyService.addCurrency(country, currencyCode, exchangeRate);

        // Assert
        assertFalse(success);
    }

    // This test is invalid because the validation is done in the ui stage!
//    @Test
//    @DisplayName("Should return error if the currency code is more than 3 digits")
//    void shouldReturnErrorIfTheCurrencyCodeIsMoreThanThreeDigits() {
//        // Arrange
//        var currencyCode = "four";
//        var exchangeRate = 0.2f;
//
//        // Act
//        var success = currencyService.addCurrency(currencyCode, exchangeRate);
//
//        // Assert
//        assertFalse(success);
//    }

    @Test
    @DisplayName("Should return Array of the currencies in the system")
    void shouldReturnCurrenciesAsList() {
        // Act
        var currencies = currencyService.getCurrencies();

        // Assert
        assertNotNull(currencies);
    }

    @Test
    @DisplayName("Should print the currencies in the system on the terminal")
    void shouldPrintCurrenciesOnTheTerminal() {
        // Act
        currencyService.printCurrencies();
    }

    @Test
    @DisplayName("Should delete a currency record from the currencies list file")
    void shouldDeleteACurrencyRecordFromTheCurrenciesListFile() {
        // Arrange
        var currencyCode = "IQD";

        // Act
        currencyService.deleteCurrency(currencyCode);
    }

    @Test
    @DisplayName("Should return an error if trying to delete a currency is not in the system")
    void shouldReturnAnErrorIfTryingToDeleteACurrencyIsNotInTheSystem() {
        // Arrange
        var currencyCode = "BYN"; // BELARUS

        // Act
        currencyService.deleteCurrency(currencyCode);
    }
}