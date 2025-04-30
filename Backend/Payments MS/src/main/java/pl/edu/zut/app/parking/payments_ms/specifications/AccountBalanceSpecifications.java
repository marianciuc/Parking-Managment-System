package pl.edu.zut.app.parking.payments_ms.specifications;

import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import pl.edu.zut.app.parking.payments_ms.entities.AccountBalance;

/**
 * Provides utility methods to build specifications for querying {@link AccountBalance} entities.
 *
 * <p>This class contains a collection of static methods that create {@link Specification} objects,
 * making it easier to filter {@link AccountBalance} entities based on various attributes such as
 * owner ID, verification status, bank account ID, Stripe account ID, and account type.
 *
 * <p>This is a utility class and is not meant to be instantiated.
 */
public class AccountBalanceSpecifications {

  private AccountBalanceSpecifications() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Creates a specification to filter {@link AccountBalance} entities by their owner ID.
   *
   * @param ownerId the UUID of the owner to filter by; if null, no filter will be applied to this
   *     property
   * @return a {@link Specification} to filter {@link AccountBalance} entities by the specified
   *     owner ID, or null if the owner ID is null
   */
  public static Specification<AccountBalance> whereOwnerIdIs(UUID ownerId) {
    return (root, query, cb) -> {
      if (ownerId == null) {
        return null;
      }
      cb.equal(root.get("ownerId"), ownerId);
      return cb.and();
    };
  }

  /**
   * Creates a specification to filter {@link AccountBalance} entities by their verification status.
   *
   * @param verified the verification status of the account balance to filter by; if null, no filter
   *     will be applied to this property
   * @return a {@link Specification} to filter {@link AccountBalance} entities by the specified
   *     verification status, or null if the verification status is null
   */
  public static Specification<AccountBalance> whereVerifiedIs(Boolean verified) {
    return ((root, query, criteriaBuilder) -> {
      if (verified == null) {
        return null;
      }
      criteriaBuilder.equal(root.get("verified"), verified);
      return criteriaBuilder.and();
    });
  }

  /**
   * Creates a specification to filter {@link AccountBalance} entities by the associated bank
   * account ID.
   *
   * @param bankAccountId the UUID of the associated bank account to filter by; if null, no filter
   *     will be applied to this property
   * @return a {@link Specification} to filter {@link AccountBalance} entities by the specified bank
   *     account ID, or null if the bank account ID is null
   */
  public static Specification<AccountBalance> whereBankAccountIdIs(UUID bankAccountId) {
    return (root, query, cb) -> {
      if (bankAccountId == null) {
        return null;
      }
      cb.equal(root.get("bankAccountId"), bankAccountId);
      return cb.and();
    };
  }

  /**
   * Creates a specification to filter {@link AccountBalance} entities by their Stripe account ID.
   *
   * @param stripeAccountId the Stripe account identifier to filter by; if null, no filter will be
   *     applied to this property
   * @return a {@link Specification} to filter {@link AccountBalance} entities by the specified
   *     Stripe account ID, or null if the Stripe account ID is null
   */
  public static Specification<AccountBalance> whereStripeAccountIdIs(String stripeAccountId) {
    return (root, query, cb) -> {
      if (stripeAccountId == null) {
        return null;
      }
      return cb.equal(root.get("stripeAccountId"), stripeAccountId);
    };
  }

  /**
   * Creates a specification to filter {@link AccountBalance} entities by account type.
   *
   * @param accountType the type of account to filter by; if null, no filter will be applied to the
   *     account type
   * @return a {@link Specification} to filter by the specified account type, or null if the account
   *     type is null
   */
  public static Specification<AccountBalance> whereAccountTypeIs(String accountType) {
    return (root, query, cb) -> {
      if (accountType == null) {
        return null;
      }
      return cb.equal(root.get("accountType"), accountType);
    };
  }

  /**
   * Builds a specification to filter {@link AccountBalance} entities based on the provided
   * parameters.
   *
   * @param verified the verification status of the account balance; if null, it will not filter by
   *     this property
   * @param bankAccountId the UUID of the associated bank account; if null, it will not filter by
   *     this property
   * @param stripeAccountId the Stripe account identifier; if null, it will not filter by this
   *     property
   * @param ownerId the UUID of the owner of the account balance; if null, it will not filter by
   *     this property
   * @param accountType the type of account to filter by; if null, it will not filter by this
   *     property
   * @return a {@link Specification} that combines all the provided filters; if all parameters are
   *     null, it returns a specification with no filters
   */
  public static Specification<AccountBalance> build(
      Boolean verified,
      UUID bankAccountId,
      String stripeAccountId,
      UUID ownerId,
      String accountType) {
    return Specification.where(whereVerifiedIs(verified))
        .and(whereBankAccountIdIs(bankAccountId))
        .and(whereStripeAccountIdIs(stripeAccountId))
        .and(whereOwnerIdIs(ownerId))
        .and(whereAccountTypeIs(accountType));
  }
}
