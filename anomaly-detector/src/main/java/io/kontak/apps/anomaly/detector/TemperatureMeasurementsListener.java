package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;

import java.util.function.Function;

@RequiredArgsConstructor
@Slf4j
public class TemperatureMeasurementsListener implements Function<KStream<String, TemperatureReading>, KStream<String, Anomaly>> {

    private final AnomalyDetectionFacade detectionFacade;

    @Override
    public KStream<String, Anomaly> apply(KStream<String, TemperatureReading> events) {

        return events.mapValues(detectionFacade::detect)
                .filter((s, anomaly) -> anomaly.isPresent())
                .mapValues((s, anomaly) -> anomaly.get())
                .selectKey((s, anomaly) -> anomaly.thermometerId())
                .peek((string, anomaly) -> log.info("Detected anomaly [{}]", anomaly));
    }
}
