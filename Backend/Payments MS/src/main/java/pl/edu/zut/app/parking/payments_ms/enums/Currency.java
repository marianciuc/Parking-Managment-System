package pl.edu.zut.app.parking.payments_ms.enums;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import lombok.Getter;

/** Enumeration representing various currencies with their codes and symbols. */
@Getter
public enum Currency {
  PLN("pln", "currency.symbol.pln", List.of("Poland"), 2.0, 100),
  EUR("eur", "currency.symbol.eur", List.of("Germany", "France", "Spain", "Italy"), 0.50, 100),
  USD("usd", "currency.symbol.usd", List.of("United States"), 0.50, 100);

  private final String code;
  private final String symbolKey;
  private final List<String> country;
  private final double minAmount;
  private final long multiplier;

  /**
   * Constructor for creating a currency with a code and symbol.
   *
   * @param code The ISO currency code (e.g., PLN, EUR, USD)
   * @param symbolKey The currency symbol key
   * @param country The country where the currency is used
   * @param minAmount The minimum amount that can be used with the currency
   * @param multiplier The multiplier for the currency
   */
  Currency(String code, String symbolKey, List<String> country, double minAmount, long multiplier) {
    this.code = code;
    this.symbolKey = symbolKey;
    this.country = country;
    this.minAmount = minAmount;
    this.multiplier = multiplier;
  }

  public String getSymbol() {
    try {
      ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
      return bundle.getString(symbolKey);
    } catch (MissingResourceException e) {
      return "[" + symbolKey + "]";
    }
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

  public long multiply(BigDecimal amount) {
    return amount.multiply(BigDecimal.valueOf(this.getMultiplier())).longValue();
  }
}
