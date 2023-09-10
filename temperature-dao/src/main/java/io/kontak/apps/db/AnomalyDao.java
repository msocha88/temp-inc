package io.kontak.apps.db;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("anomaly")
public record AnomalyDao(
        double temperature,
        @Indexed
        String roomId,
        @Indexed
        String thermometerId,
        Instant timestamp) {
}
