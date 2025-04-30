package pl.edu.zut.app.parking.payments_ms.entities;

import jakarta.persistence.Column;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionMetadata {

    @Column(name = "stripe_payment_id")
    private String stripePaymentId;

    @Column(name = "subscription_order_id")
    private UUID subscriptionOrderId;

    @Column(name = "session_id")
    private UUID sessionId;

    @Column(name = "parking_id")
    private UUID parkingId;
}
