package pl.edu.zut.parking.app.gateway.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "auth-service")
public interface SecurityServiceClient {

    @GetMapping("/api/v1/security/jwt/public-key")
    String getPublicKey();
}
