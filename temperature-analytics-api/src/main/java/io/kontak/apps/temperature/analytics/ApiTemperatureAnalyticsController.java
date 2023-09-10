package io.kontak.apps.temperature.analytics;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.temperature.analytics.api.TemperatureAnalyticsApi;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
public class ApiTemperatureAnalyticsController implements TemperatureAnalyticsApi {

    @Autowired
    private AnomaliesFacade anomaliesFacade;
    @Override
    public Collection<Anomaly> getAnomaliesByRoomId(String roomId) {
        return anomaliesFacade.getAnomaliesByRoomId(roomId);
    }

    @Override
    public Collection<Anomaly> getAnomaliesByThermometerId(String thermometerId) {
        return anomaliesFacade.getAnomaliesByThermometerId(thermometerId);
    }

    @Override
    public Collection<String> getThermometersWithAnomaliesOver(Integer threshold) {
        return anomaliesFacade.getAnomaliesOverThreshold(threshold);
    }
}
