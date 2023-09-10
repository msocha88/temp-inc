package io.kontak.apps.temperature.analytics.api;

import io.kontak.apps.event.Anomaly;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequestMapping("/api/anomalies/")
public interface TemperatureAnalyticsApi {


    @GetMapping("room/{roomId}")
    Collection<Anomaly> getAnomaliesByRoomId(@PathVariable("roomId") String roomId);

    @GetMapping("thermometer/{thermometerId}")
    Collection<Anomaly> getAnomaliesByThermometerId(@PathVariable("thermometerId") String roomId);

    @GetMapping("overThreshold")
    Collection<String> getThermometersWithAnomaliesOver(@Nullable @RequestParam Integer threshold);

}
