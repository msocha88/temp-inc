package io.kontak.apps.temperature.generator;

import io.kontak.apps.temperature.generator.model.Thermometer;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "temperature-generator")
@Data
public class TemperatureGeneratorProperties {

    private double anomalyRate;
    private List<Thermometer> thermometers;

}
