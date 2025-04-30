package pl.edu.zut.app.parking.payments_ms.services;

import java.math.BigDecimal;
import pl.edu.zut.app.parking.payments_ms.entities.ConversionDetails;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;
import pl.edu.zut.app.parking.payments_ms.exceptions.ExchangeException;

/**
 * The ExchangeService interface provides functionality for handling currency exchanges. It defines
 * a method to convert a specified amount from one currency to another, returning details about the
 * exchange, including the exchange rate and exchanged amount.
 */
public interface ExchangeService {

  /**
   * Exchanges a specific amount from one currency to another and returns the exchange rate details.
   *
   * @param from The currency to exchange from.
   * @param to The currency to exchange to.
   * @param amount The amount to be exchanged.
   * @return An ExchangeRate object representing the details of the exchange, including rate and
   *     exchanged amount.
   * @throws ExchangeException If the exchange process fails, such as due to unsupported currencies
   *     or invalid input.
   */
  ConversionDetails exchange(Currency from, Currency to, BigDecimal amount)
      throws ExchangeException;
}
