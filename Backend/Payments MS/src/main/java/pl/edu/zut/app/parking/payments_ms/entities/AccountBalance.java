package pl.edu.zut.app.parking.payments_ms.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.edu.zut.app.parking.payments_ms.enums.AccountType;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;
import pl.edu.zut.app.parking.payments_ms.exceptions.BalanceAccountInvalidOperationException;
import pl.edu.zut.app.parking.payments_ms.exceptions.InsufficientFundsException;

/** Represents the account balance and associated details of a user's account. */
@Getter
@Setter
@SuperBuilder
@Entity
@RequiredArgsConstructor
@Table(
    name = "account_balances",
    uniqueConstraints = {@UniqueConstraint(columnNames = "owner_id")})
public class AccountBalance extends AbstractBaseEntity {

  @Column(name = "system_currency", updatable = false)
  private Currency systemCurrency;

  @Enumerated(EnumType.STRING)
  @Column(name = "preferred_currency", nullable = false)
  private Currency preferredCurrency;

  @Builder.Default
  @Column(name = "balance", nullable = false)
  private BigDecimal balance = BigDecimal.ZERO;

  @Column(name = "owener_id", nullable = false, unique = true, updatable = false)
  private UUID ownerId;

  @Enumerated(EnumType.STRING)
  @Column(name = "account_type", nullable = false, updatable = false)
  private AccountType accountType;

  @Column(name = "stripe_account_id", length = 255)
  private String stripeAccountId;

  @OneToMany(
      mappedBy = "accountBalance",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  List<BankAccount> bankAccounts;

  @Column(name = "is_verified_account")
  private Boolean isVerifiedAccount;

  public void setAccountType(AccountType accountType) {
    throw new UnsupportedOperationException("Account type cannot be changed");
  }

  public void verifyAccount() {
    isVerifiedAccount = true;
  }

  public void unverifyAccount() {
    isVerifiedAccount = false;
  }

  /**
   * Adds the specified amount to the account balance.
   *
   * @param amount the amount to be deposited; must be greater than zero
   * @throws BalanceAccountInvalidOperationException if the specified amount is less than or equal
   *     to zero
   */
  public void deposit(BigDecimal amount) throws BalanceAccountInvalidOperationException {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new BalanceAccountInvalidOperationException("Amount must be greater than zero");
    }
    balance = balance.add(amount);
  }

  /**
   * Withdraws the specified amount from the account balance.
   *
   * @param amount the amount to be withdrawn; must be greater than zero and less than or equal to
   *     the current balance
   * @throws InsufficientFundsException if the amount to be withdrawn exceeds the current balance
   */
  public void withdraw(BigDecimal amount) throws InsufficientFundsException {
    if (balance.compareTo(amount) < 0) {
      throw new InsufficientFundsException("Insufficient funds to carry out this action");
    }
    balance = balance.subtract(amount);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    AccountBalance that = (AccountBalance) o;
    return systemCurrency == that.systemCurrency
        && preferredCurrency == that.preferredCurrency
        && Objects.equals(balance, that.balance)
        && Objects.equals(ownerId, that.ownerId)
        && accountType == that.accountType
        && Objects.equals(stripeAccountId, that.stripeAccountId)
        && Objects.equals(bankAccounts, that.bankAccounts)
        && Objects.equals(isVerifiedAccount, that.isVerifiedAccount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        super.hashCode(),
        systemCurrency,
        preferredCurrency,
        balance,
        ownerId,
        accountType,
        stripeAccountId,
        bankAccounts,
        isVerifiedAccount);
  }
}
