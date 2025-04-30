package pl.edu.zut.app.parking.admin_ms.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
@Slf4j
public class KafkaHostLogger {

    @Value("${spring.kafka.bootstrap-servers:1234}")
    private String kafkaHost;

    @PostConstruct
    public void logKafkaHost() {
        log.info("Resolved SPRING_KAFKA_HOST = " + kafkaHost);
    }
}
