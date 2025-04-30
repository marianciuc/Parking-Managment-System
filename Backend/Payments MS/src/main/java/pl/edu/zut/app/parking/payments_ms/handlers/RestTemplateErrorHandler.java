package pl.edu.zut.app.parking.payments_ms.handlers;

import static org.springframework.http.HttpStatus.*;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import pl.edu.zut.app.parking.payments_ms.exceptions.*;

@Component
@Slf4j
public class RestTemplateErrorHandler {

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setErrorHandler(
        new DefaultResponseErrorHandler() {
          @Override
          public void handleError(ClientHttpResponse response) throws IOException {
            String statusText = response.getStatusText();
            int statusCode = response.getStatusCode().value();

            switch (response.getStatusCode()) {
              case NOT_FOUND:
                throw new NotFoundOpenExchangeException();
              case UNAUTHORIZED:
                throw new MissingAppIdOpenExchangeException();
              case TOO_MANY_REQUESTS:
                throw new NotAllowedOpenExchangeException();
              case FORBIDDEN:
                throw new AccessRestrictedExchangeException("Access restricted");
              case BAD_REQUEST:
                throw new InvalidBaseOpenExchangeException(statusText);
              default:
                log.warn("Unexpected response status: {} - {}", statusCode, statusText);
                throw new ExchangingProcessException(
                    "Unexpected HTTP response: " + statusCode + " - " + statusText);
            }
          }
        });
    return restTemplate;
  }
}
