package io.kontak.apps.anomaly.storage.config;


import io.kontak.apps.anomaly.storage.AnomalyListener;
import io.kontak.apps.anomaly.storage.StorageFacade;
import io.kontak.apps.event.Anomaly;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class KafkaConfig {

    @Bean
    public Consumer<KStream<String, Anomaly>> anomalyConsumer(StorageFacade storageFacade) {
        return new AnomalyListener(storageFacade);
    }
}
