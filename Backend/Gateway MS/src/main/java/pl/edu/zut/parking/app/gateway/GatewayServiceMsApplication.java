package pl.edu.zut.parking.app.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import pl.edu.zut.parking.app.gateway.config.GatewayCorsConfig;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableConfigurationProperties(GatewayCorsConfig.class)
public class GatewayServiceMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceMsApplication.class, args);
	}

}
