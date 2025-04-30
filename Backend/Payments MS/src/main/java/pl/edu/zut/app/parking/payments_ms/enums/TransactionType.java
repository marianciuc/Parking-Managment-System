package pl.edu.zut.app.parking.payments_ms.enums;

/**
 * Enumeration representing various types of transactions that can occur within
 * the payment system.
 *
 * INTERNAL_TRANSFER - A transaction that moves funds between accounts within
 * the same financial institution.
 *
 * EXTERNAL_DEPOSIT - A transaction where funds are deposited from an external
 * source into an account within the system.
 *
 * PAYMENT - A transaction where funds are paid out to a third party or service.
 *
 * WITHDRAWAL - A transaction that involves the withdrawal of funds from an
 * account.
 */
public enum TransactionType {
    INTERNAL_TRANSFER,
    WITHDRAWAL,
    INTERNAL_TRANSFER_REFUND,
    EXTERNAL_DEPOSIT,
    SESSIONS_PAYMENT,
    SESSIONS_REFUND,
    SUBSCRIPTION_PAYMENT,
    SUBSCRIPTION_REFUND, SESSION_EXTERNAL_PAYMENT,
}
