package pl.edu.zut.app.parking.notifications;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class NotificationsServiceApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

//	@Container
//	static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.3")
//			.withDatabaseName("test_db")
//			.withUsername("root")
//			.withPassword("root");
//
//	@DynamicPropertySource
//	static void overrideProperties(DynamicPropertyRegistry registry) {
//		registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
//		registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
//		registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
//	}
//
//	@Autowired
//	private TestRestTemplate restTemplate;

//	@Test
//	void contextLoads() {
//		assertThat(postgreSQLContainer.isRunning()).isTrue();
//	}

	@Test
	void contextLoads() {
		Assertions.assertNotNull(applicationContext);
	}
}