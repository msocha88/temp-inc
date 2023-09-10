package io.kontak.apps.anomaly.storage;

import io.kontak.apps.event.Anomaly;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class AnomalyListener implements Consumer<KStream<String, Anomaly>> {

    @Autowired
    private final StorageFacade storageFacade;

    @Override
    public void accept(KStream<String, Anomaly> stringAnomalyKStream) {
        stringAnomalyKStream.foreach((k, anomaly) -> storageFacade.store(anomaly));
    }
}
