package pl.edu.zut.app.parking.payments_ms.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "account_balance_stats")
@RequiredArgsConstructor
public class AccountBalanceStats extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_balance_id", nullable = false)
    AccountBalance accountBalance;

    @Column(name = "current_balance", nullable = false)
    BigDecimal currentBalance;

    @Column(name = "blocked_amount", nullable = false)
    BigDecimal blockedAmount;

    @Column(name = "withdrawn_amount", nullable = false)
    BigDecimal withdrawnAmount;
}
