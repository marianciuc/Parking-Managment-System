package pl.edu.zut.app.parking.payments_ms.services;

import java.util.List;
import java.util.UUID;
import pl.edu.zut.app.parking.payments_ms.dto.common.BankAccountDto;
import pl.edu.zut.app.parking.payments_ms.exceptions.BankAccountCreationException;
import pl.edu.zut.app.parking.payments_ms.exceptions.BankAccountNotFoundException;

/**
 * A service for managing bank accounts. It provides functionality to create, retrieve, delete, and
 * list bank accounts associated with a user.
 */
public interface BankAccountService {

  /**
   * Creates a new bank account for the user specified by the given unique identifier.
   *
   * @param bankAccount A {@code BankAccountDto} object containing the details of the bank account to be created.
   * @param userId The unique identifier of the user for whom the bank account is being created.
   * @return A {@code BankAccountDto} representing the newly created bank account.
   * @throws BankAccountCreationException If an error occurs during the creation of the bank account.
   */
  BankAccountDto createBankAccount(BankAccountDto bankAccount, UUID userId) throws BankAccountCreationException;

  /**
   * Retrieves the bank account details for the specified bank account identifier.
   *
   * @param bankAccountId The unique identifier of the bank account to be retrieved.
   * @return A {@code BankAccountDto} containing the details of the requested bank account.
   * @throws BankAccountNotFoundException If no bank account is found for the given identifier.
   */
  BankAccountDto getBankAccount(UUID bankAccountId) throws BankAccountNotFoundException;

  /**
   * Deletes the bank account associated with the specified unique identifier.
   *
   * @param bankAccountId The unique identifier of the bank account to be deleted.
   * @throws BankAccountNotFoundException If no bank account is found for the given identifier.
   */
  void deleteBankAccount(UUID bankAccountId) throws BankAccountNotFoundException;

  /**
   * Retrieves a list of bank accounts associated with the specified user.
   *
   * @param userId The unique identifier of the user whose bank accounts are to be retrieved.
   * @return A list of {@code BankAccountDto} objects containing details of the user's bank
   *     accounts.
   */
  List<BankAccountDto> getUserBankAccounts(UUID userId);
}
