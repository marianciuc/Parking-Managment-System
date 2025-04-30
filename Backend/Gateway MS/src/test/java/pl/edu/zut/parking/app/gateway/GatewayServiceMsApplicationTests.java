package pl.edu.zut.parking.app.gateway;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pl.edu.zut.parking.app.gateway.clients.SecurityServiceClient;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
class GatewayServiceMsApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Configuration
	static class TestConfig {
		@Bean
		@Primary
		public JWSVerifier testJwsVerifier() throws Exception {
			String testPublicKey = "{\"kty\":\"RSA\",\"e\":\"AQAB\",\"kid\":\"Outl4hhj-Ne-2J1OeK6x2CZUM1RNayGFef677upqJRw\",\"n\":\"vkIo2IdIaARPhoWkPziGJiKsqB-HHhVCrLZO9QORzu1YPgiqj5MDaGqyzHo4EyIPOrIBV9qbl9zfaESuFZXaV00rrDx8drIreE_yNMr_ihdJqyyezsefpuYr_9c1nq7Tzh4l7hkDq3ER1moyx-vdiuwjv5xn7qh45jhNBun8XLUaIN4xvPoH6dYQJjHBuJscryX0OwW3N6LKLCTHk2L7c3TKwrPtwPVN7UL1-_atTDrdWVHL3KqzGup-XnGfAOUiu9sPOXTyFZcggwcjQoUEfdp00mhGFMvUqdZfxts3Jc-wgTOAN0hd_775HKmpq6znVOM33b4JedZD1RT0m9ZTtQ\"}";
			RSAKey parsedRSAKey = RSAKey.parse(testPublicKey);
			return new RSASSAVerifier(parsedRSAKey);
		}
	}



	@Test
	void contextLoads() {
		Assertions.assertNotNull(applicationContext);
	}

}
