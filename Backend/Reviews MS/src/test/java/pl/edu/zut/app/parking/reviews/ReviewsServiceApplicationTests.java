package pl.edu.zut.app.parking.reviews;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ReviewsServiceApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		assertThat(applicationContext).isNotNull();
	}

	@Test
	void shouldLoadServiceBeans() {
		assertThat(applicationContext.containsBean("languageDetectionServiceImpl")).isTrue();
	}

	@Test
	void shouldLoadControllerBeans() {
		assertThat(applicationContext.containsBean("reviewController")).isTrue();
	}

	@Test
	void shouldLoadRepositoryBeans() {
		assertThat(applicationContext.containsBean("reviewsRepository")).isTrue();
	}

	@Test
	void applicationStartsSuccessfully() {
		ReviewsServiceApplication.main(new String[] {});
	}
}
