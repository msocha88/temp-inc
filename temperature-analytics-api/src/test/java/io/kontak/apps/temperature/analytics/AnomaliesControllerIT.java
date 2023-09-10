package io.kontak.apps.temperature.analytics;

import io.kontak.apps.event.Anomaly;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.util.UUID;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AnomaliesControllerIT extends AbstractIntegrationTest {

    @Autowired
    MongoTemplate mongoTemplate;


    @AfterEach
    public void afterEach() {
        mongoTemplate.dropCollection(Anomaly.class);
    }

    @Test
    public void shouldReturnAnomalyByThermometerId() {
        //given
        var thermometerId = UUID.randomUUID().toString();
        var anomaly = new Anomaly(24, "roomId", thermometerId, Instant.now());
        mongoTemplate.save(anomaly);
        //when
        get("/api/anomalies/thermometer/" + thermometerId)

                //then
                .then()
                .statusCode(200)
                .assertThat()
                .body("[0].thermometerId", equalTo(thermometerId));
    }


    @Test
    public void shouldReturnMultipleAnomaliesByThermometerId() {
        //given
        var thermometerId = UUID.randomUUID().toString();
        var anomaly = new Anomaly(24, "roomId", thermometerId, Instant.now());
        var anomaly2 = new Anomaly(26, "roomId", thermometerId, Instant.now());
        mongoTemplate.save(anomaly);
        mongoTemplate.save(anomaly2);

        //when
        get("/api/anomalies/thermometer/" + thermometerId)
                //then
                .then()
                .statusCode(200)
                .assertThat()
                .body("size()", is(2));
    }

    @Test
    public void shouldReturnAnomalyByRoomId() {
        //given
        var roomId = UUID.randomUUID().toString();
        var anomaly = new Anomaly(24, roomId, "thermometerId", Instant.now());
        mongoTemplate.save(anomaly);
        //when
        get("/api/anomalies/room/" + roomId)
                //then
                .then()
                .statusCode(200)
                .assertThat()
                .body("[0].roomId", equalTo(roomId));
    }


    @Test
    public void shouldReturnMultipleAnomaliesByRoomId() {
        //given
        var roomId = UUID.randomUUID().toString();
        var anomaly = new Anomaly(24, roomId, "thermometerId", Instant.now());
        var anomaly2 = new Anomaly(24, roomId, "thermometerId", Instant.now());
        mongoTemplate.save(anomaly);
        mongoTemplate.save(anomaly2);
        //when
        get("/api/anomalies/room/" + roomId)
                //then
                .then()
                .statusCode(200)
                .assertThat()
                .body("size()", is(2));
    }

    @Test
    public void shouldNotReturnThermometersIdAsTheresNoOverThreshold() {

        //given
        var thermometerId = UUID.randomUUID().toString();
        var anomaly = new Anomaly(24, "roomId", thermometerId, Instant.now());
        mongoTemplate.save(anomaly);
        mongoTemplate.save(anomaly);
        //when
        get("/api/anomalies/overThreshold")
                .then()
                .body("$", empty());
    }


    @Test
    public void shouldReturnThermometersIdWithPassedThreshold() {
        //given
        var thermometerId = UUID.randomUUID().toString();
        var anomaly = new Anomaly(24, "roomId", thermometerId, Instant.now());
        mongoTemplate.save(anomaly);
        mongoTemplate.save(anomaly);
        mongoTemplate.save(anomaly);
        mongoTemplate.save(anomaly);

        //when
        given().param("threshold", 3)
                .when()
                .get("/api/anomalies/overThreshold")
                .then()
                .body("$", contains(thermometerId));
    }
}
