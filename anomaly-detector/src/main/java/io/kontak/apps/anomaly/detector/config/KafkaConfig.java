package io.kontak.apps.anomaly.detector.config;

import io.kontak.apps.anomaly.detector.AnomalyDetectionFacade;
import io.kontak.apps.anomaly.detector.TemperatureMeasurementsListener;
import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class KafkaConfig {

    @Bean
    public Function<KStream<String, TemperatureReading>, KStream<String, Anomaly>> anomalyDetectorProcessor(
            AnomalyDetectionFacade detectionFacade)
    {
        return new TemperatureMeasurementsListener(detectionFacade);
    }

}
