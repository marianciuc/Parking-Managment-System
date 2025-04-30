package pl.edu.zut.app.parking.payments_ms.integrations;

import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.payments_ms.dto.KYC;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;

/**
 * Service responsible for integrating with Stripe's APIs to handle various payment-related
 * operations such as creating payment intents, managing connected accounts, handling payouts, and
 * verifying accounts.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StripeIntegrationService {

  /**
   * Creates a payment intent using the provided amount, currency, and transaction ID.
   *
   * @param amount The amount for the payment intent to be created, represented as a BigDecimal.
   * @param currency The currency in which the payment will be made, represented as a Currency enum.
   * @param transactionId A UUID representing the transaction for which the payment intent is
   *     created.
   * @return The client secret of the created payment intent, which can be used to confirm the
   *     payment on the client-side.
   * @throws StripeException If an error occurs while communicating with the Stripe API.
   */
  public String createPaymentIntent(BigDecimal amount, Currency currency, UUID transactionId)
      throws StripeException {
    PaymentIntentCreateParams params =
        PaymentIntentCreateParams.builder()
            .setAmount(currency.multiply(amount))
            .setCurrency(currency.getCode())
            .setDescription(transactionId.toString())
            .setConfirm(false)
            .addAllPaymentMethodType(List.of("card", "blik", "p24"))

            .build();
    log.info("Payment intent params: {}", params);

    PaymentIntent paymentIntent = PaymentIntent.create(params);

    log.info("Payment intent: {}", paymentIntent);
    return paymentIntent.getClientSecret();
  }

  /**
   * Creates a new Stripe account with the specified email and country.
   *
   * @param email the email address to associate with the account
   * @param country the country code for the Stripe account
   * @return the ID of the created Stripe account
   * @throws StripeException if an error occurs during account creation
   */
  public String createAccount(String email, String country) throws StripeException {
    log.info("Creating Stripe account for email: {} and country: {}", email, country);
    AccountCreateParams params =
        AccountCreateParams.builder()
            .setType(AccountCreateParams.Type.CUSTOM)
            .setCountry(country)
            .setEmail(email)
            .setBusinessType(AccountCreateParams.BusinessType.INDIVIDUAL)
            .setCapabilities(
                AccountCreateParams.Capabilities.builder()
                    .setCardPayments(
                        AccountCreateParams.Capabilities.CardPayments.builder()
                            .setRequested(true)
                            .build())
                    .setTransfers(
                        AccountCreateParams.Capabilities.Transfers.builder()
                            .setRequested(true)
                            .build())
                    .build())
            .build();

    Account account = Account.create(params);
    System.out.println("Connected Account created: " + account.getId());
    return account.getId();
  }

  /**
   * Creates an IBAN payment method for a connected account.
   *
   * @param connectedAccountId The ID of the connected account to which the IBAN payment method is
   *     being added.
   * @param iban The IBAN (International Bank Account Number) of the bank account to be added.
   * @param fullname The full name of the account holder.
   * @param currency The currency of the bank account.
   * @param country The country in which the bank account is located.
   * @return The ID of the newly created IBAN payment method.
   * @throws StripeException If an error occurs while creating the IBAN payment method.
   */
  public String createIbanPaymentMethod(
      String connectedAccountId, String iban, String fullname, Currency currency, String country)
      throws StripeException {
    Map<String, Object> bankAccountParams = new HashMap<>();
    bankAccountParams.put("object", "bank_account");
    bankAccountParams.put("country", country);
    bankAccountParams.put("currency", currency.getCode());
    bankAccountParams.put("account_holder_name", fullname);
    bankAccountParams.put("account_holder_type", "individual");
    bankAccountParams.put("iban", iban);

    Map<String, Object> params = new HashMap<>();
    params.put("external_account", bankAccountParams);

    Account account = Account.retrieve(connectedAccountId);
    ExternalAccount bankAccount = account.getExternalAccounts().create(params);

    log.info("Bank account created: {}", bankAccount);
    return bankAccount.getId();
  }

  /**
   * Deletes a specific bank account associated with a connected account in the Stripe system.
   *
   * @param connectedAccountId The unique identifier of the connected account to which the bank
   *     account belongs.
   * @param bankAccountId The unique identifier of the bank account to be deleted.
   * @throws StripeException If an error occurs while retrieving or deleting the bank account.
   */
  public void deleteBankAccount(String connectedAccountId, String bankAccountId)
      throws StripeException {
    Account account = Account.retrieve(connectedAccountId);
    ExternalAccount bankAccount = account.getExternalAccounts().retrieve(bankAccountId);
    bankAccount.delete();
  }

  /**
   * Creates a payout to a specified bank account with the given amount and currency. This method
   * initializes a payout request to the banking destination linked with the provided bank account
   * ID.
   *
   * @param bankAccountId the ID of the bank account to which the payout will be sent
   * @param amount the amount to be transferred as a payout
   * @param currency the currency of the payout
   * @param transactionId the unique identifier for this transaction, used as a description for the
   *     payout
   * @return the ID of the created payout
   * @throws StripeException if an error occurs while creating the payout
   */
  public String createPayout(
      String bankAccountId, BigDecimal amount, Currency currency, UUID transactionId)
      throws StripeException {
    log.info("Creating payout for bank account: {} and amount: {}", bankAccountId, amount);
    PayoutCreateParams params =
        PayoutCreateParams.builder()
            .setAmount(currency.multiply(amount))
            .setCurrency(currency.getCode())
            .setDestination(bankAccountId)
            .setDescription(transactionId.toString())
            .setMethod(PayoutCreateParams.Method.STANDARD)
            .build();

    Payout payout = Payout.create(params);
    log.info("Payout: {}", payout);
    return payout.getId();
  }

  /**
   * Verifies and updates the information of a connected account with the provided KYC details. This
   * method updates the connected account's individual details such as name, email, address, date of
   * birth, and national ID number.
   *
   * @param connectedAccountId the ID of the connected account to be updated
   * @param kyc the KYC information containing the individual's details required for updating the
   *     connected account
   * @throws StripeException if an error occurs during the update process with the Stripe API
   */
  public void verifyConnectedAccount(String connectedAccountId, KYC kyc) throws StripeException {
    AccountUpdateParams params =
        AccountUpdateParams.builder()
            .setIndividual(
                AccountUpdateParams.Individual.builder()
                    .setFirstName(kyc.firstname())
                    .setLastName(kyc.lastname())
                    .setEmail(kyc.email())
                    .setIdNumber(kyc.nationalIdNumber())
                    .setAddress(
                        AccountUpdateParams.Individual.Address.builder()
                            .setCity(kyc.address().city())
                            .setCountry(kyc.address().countryCode())
                            .setLine1(kyc.address().line1())
                            .setPostalCode(kyc.address().postalCode())
                            .setState(kyc.address().state())
                            .build())
                    .setDob(
                        AccountUpdateParams.Individual.Dob.builder()
                            .setDay(kyc.dob().day())
                            .setMonth(kyc.dob().month())
                            .setYear(kyc.dob().year())
                            .build())
                    .build())
            .build();

    Account account = Account.retrieve(connectedAccountId);
    account.update(params);
    log.info("Connected account updated: {}", account);
  }
}
