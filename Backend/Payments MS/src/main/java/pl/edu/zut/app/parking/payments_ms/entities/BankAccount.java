package pl.edu.zut.app.parking.payments_ms.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Entity
@RequiredArgsConstructor
@Table(name = "bank_accounts")
public class BankAccount extends AbstractBaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_balance_id", nullable = false)
    private AccountBalance accountBalance;

    @Column(name = "iban", nullable = false, length = 34)
    private String iban;

    @Column(name = "bank_name", length = 255)
    private String bankName;

    @Column(name = "fullname", length = 255)
    private String fullname;

    @Column(name = "country", length = 2)
    private String country;

    @Column(name = "pm_id")
    private String paymentMethodId;
}
