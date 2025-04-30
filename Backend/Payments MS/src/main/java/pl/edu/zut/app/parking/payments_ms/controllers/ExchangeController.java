package pl.edu.zut.app.parking.payments_ms.controllers;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.zut.app.parking.payments_ms.entities.ConversionDetails;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;
import pl.edu.zut.app.parking.payments_ms.services.ExchangeService;

@RestController
@RequestMapping("/api/v1/exchange")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService exchangeService;

    @GetMapping
    public ResponseEntity<ConversionDetails> getExchangeRate(@RequestParam Currency from, @RequestParam Currency to,
                                                             @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(exchangeService.exchange(from, to, amount));
    }
}
