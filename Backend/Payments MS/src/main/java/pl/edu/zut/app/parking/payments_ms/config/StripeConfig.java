package pl.edu.zut.app.parking.payments_ms.config;

import com.stripe.Stripe;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

  @Value("${stripe.api.key}")
  private String stripeApiKey;

  @PostConstruct
  public void initStripe() {
    Stripe.apiKey = stripeApiKey;
  }
}
