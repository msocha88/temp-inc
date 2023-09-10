package io.kontak.apps.temperature.analytics;

import io.kontak.apps.event.Anomaly;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(classes = TemperatureAnalyticsApplication.class,
        webEnvironment = WebEnvironment.DEFINED_PORT)
@Testcontainers
public class AbstractIntegrationTest {

    public final static MongoDBContainer mongoContainer;

    static {
        mongoContainer = new MongoDBContainer("mongo:6-jammy").withExposedPorts(27017);
        mongoContainer.start();
    }

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoContainer::getReplicaSetUrl);
    }
}
