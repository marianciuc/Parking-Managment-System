package pl.edu.zut.app.parking.payments_ms.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    BLIK("blik"),
    CARD("card"),
    SEPA_DEBIT("sepa_debit"),
    P24("p24"),
    APPLE_PAY("apple_pay"),
    GOOGLE_PAY("google_pay");

    private final String type;

    PaymentMethod(String type) {
        this.type = type;
    }
}
