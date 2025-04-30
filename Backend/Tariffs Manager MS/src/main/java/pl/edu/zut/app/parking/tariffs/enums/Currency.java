package pl.edu.zut.app.parking.tariffs.enums;

import lombok.Getter;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Enumeration representing various currencies with their codes and symbols.
 */
@Getter
public enum Currency {
    PLN("PLN", "currency.symbol.pln", List.of("Poland")),
    EUR("EUR", "currency.symbol.eur", List.of("Germany", "France", "Spain", "Italy")),
    USD("USD", "currency.symbol.usd", List.of("United States"));

    private final String code;
    private final String symbolKey;
    private final List<String> country;

    /**
     * Constructor for creating a currency with a code and symbol.
     *
     * @param code      The ISO currency code (e.g., PLN, EUR, USD)
     * @param symbolKey The currency symbol key
     * @param country
     */
    Currency(String code, String symbolKey, List<String> country) {
        this.code = code;
        this.symbolKey = symbolKey;
        this.country = country;
    }

    public String getSymbol() {
        return ResourceBundle.getBundle("messages").getString(symbolKey);
    }

    /**
     * Get a currency by its code.
     *
     * @param code The code of the currency.
     * @return The matching currency enum.
     * @throws IllegalArgumentException If the code does not match any currency.
     */
    public static Currency fromCode(String code) {
        for (Currency currency : values()) {
            if (currency.getCode().equalsIgnoreCase(code)) {
                return currency;
            }
        }
        throw new IllegalArgumentException("Unknown currency code: " + code);
    }

    /**
     * Retrieves the currency associated with the specified country.
     *
     * @param country The name of the country for which the currency is to be retrieved.
     * @return The currency associated with the specified country.
     * @throws IllegalArgumentException If no matching currency is found for the given country.
     */
    public static Currency fromCountry(String country) {
        for (Currency currency : values()) {
            if (currency.getCountry().contains(country)) {
                return currency;
            }
        }
        throw new IllegalArgumentException("Unknown country: " + country);
    }

}
