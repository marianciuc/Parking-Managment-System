package pl.edu.zut.app.parking.payments_ms.services;

import java.util.UUID;
import org.springframework.data.domain.Page;
import pl.edu.zut.app.parking.payments_ms.dto.KYC;
import pl.edu.zut.app.parking.payments_ms.dto.common.AccountBalanceDto;
import pl.edu.zut.app.parking.payments_ms.dto.common.TransactionDto;
import pl.edu.zut.app.parking.payments_ms.enums.AccountType;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;
import pl.edu.zut.app.parking.payments_ms.exceptions.*;

/**
 * Service interface for managing account balances. Provides methods for creating, updating, and
 * retrieving account balances, as well as handling associated currencies and transactions.
 */
public interface AccountBalanceService {

  /**
   * Updates the account balance based on the details of the provided transaction.
   *
   * @param transaction the transaction details used to update the account balance, containing
   *     information about the source, destination accounts, and the transaction type
   * @throws AccountBalanceInvalidArgumentsException if the provided transaction contains invalid
   *     arguments
   * @throws InsufficientFundsException if there are insufficient funds in the source account for
   *     the transaction
   * @throws AccountBalanceNotFoundException if the account balance for the source or destination
   *     account cannot be found
   */
  void updateAccountBalance(TransactionDto transaction)
      throws AccountBalanceInvalidArgumentsException,
          InsufficientFundsException,
          AccountBalanceNotFoundException;

  /**
   * Creates an account balance for the specified account with the given account type.
   *
   * @param accountId the unique identifier of the account for which the balance is to be created
   * @param accountType the type of the account, represented as an {@link AccountType} enum
   * @throws AccountBalanceAlreadyExistsException if an account balance already exists for the
   *     specified account
   */
  void createAccountBalance(UUID accountId, AccountType accountType)
      throws AccountBalanceAlreadyExistsException;

  /**
   * Retrieves the currency associated with the specified owner.
   *
   * @param ownerId the unique identifier of the owner whose currency is to be retrieved
   * @return the {@link Currency} associated with the specified owner
   * @throws AccountBalanceNotFoundException if the account balance for the specified owner is not
   *     found
   */
  Currency getOwnerCurrency(UUID ownerId) throws AccountBalanceNotFoundException;

  /**
   * Updates the currency associated with the specified owner.
   *
   * @param ownerId the unique identifier of the owner whose currency is to be updated
   * @param currency the new currency to be assigned to the owner, represented as a string
   * @return the updated {@link Currency} of the owner
   * @throws AccountBalanceInvalidArgumentsException if the provided arguments are invalid
   * @throws AccountBalanceNotFoundException if the account balance for the specified owner is not
   *     found
   */
  Currency updateOwnerCurrency(UUID ownerId, String currency)
      throws AccountBalanceInvalidArgumentsException, AccountBalanceNotFoundException;

  /**
   * Retrieves the account balance for a specific owner.
   *
   * @param ownerId the unique identifier of the owner whose account balance is to be retrieved
   * @param converted a flag indicating whether the balance should be converted to account preferred
   *     currency
   * @return an {@link AccountBalanceDto} representing the account balance details of the owner
   */
  AccountBalanceDto getOwnerAccountBalance(UUID ownerId, Boolean converted);

  /**
   * Retrieves the account balance for a specific account.
   *
   * @param accountId the unique identifier of the account whose balance is to be retrieved
   * @param converted a flag indicating whether the balance should be converted to the account's
   *     preferred currency
   * @return an {@link AccountBalanceDto} representing the account balance details of the specified
   *     account
   */
  AccountBalanceDto getAccountBalance(UUID accountId, Boolean converted);

  /**
   * Retrieves a pageable list of account balances based on the specified filters and sorting
   * options.
   *
   * @param size the number of items per page
   * @param page the current page index (zero-based)
   * @param sort the property used to sort the results
   * @param direction the direction of sorting, either "ASC" or "DESC"
   * @param ownerId the unique identifier of the account owner to filter by
   * @param verified a flag indicating whether to filter by verified accounts
   * @param accountId the unique identifier of a specific account to filter by
   * @param accountType the type of account to filter by
   * @param converted a flag indicating whether the balances should be converted to the account's
   *     preferred currency
   * @param stripeAccountId the Stripe account ID to filter by
   * @return a page of {@link AccountBalanceDto} objects representing the account balances matching
   *     the specified criteria
   */
  Page<AccountBalanceDto> findAccountBalances(
      int size,
      int page,
      String sort,
      String direction,
      String ownerId,
      Boolean verified,
      String accountId,
      String accountType,
      Boolean converted,
      String stripeAccountId);

  /**
   * Verifies the user's account based on the provided owner ID and KYC information.
   *
   * @param ownerId the unique identifier of the owner whose account is to be verified
   * @param kyc the KYC (Know Your Customer) information required for verification,
   *            including personal and address details
   * @return an {@link AccountBalanceDto} representing the details of the verified account
   * @throws AccountBalanceVerificationException if the account verification process fails
   * @throws AccountBalanceAlreadyVerifiedException if the account has already been verified
   */
  AccountBalanceDto verifyUserAccount(UUID ownerId, KYC kyc)
      throws AccountBalanceVerificationException, AccountBalanceAlreadyVerifiedException;
}
