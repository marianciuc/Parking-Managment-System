package pl.edu.zut.app.parking.payments_ms.enums;

/**
 * Enumeration representing the status of a transaction within the payment system.
 *
 * PENDING - Indicates that the transaction is in progress and has not yet been completed.
 * COMPLETED - Indicates that the transaction has been successfully completed.
 * FAILED - Indicates that the transaction was not successful and has failed.
 */
public enum TransactionStatus {
    PENDING,
    COMPLETED,
    FAILED,
    CANCELLED,
    REFUNDED
}
