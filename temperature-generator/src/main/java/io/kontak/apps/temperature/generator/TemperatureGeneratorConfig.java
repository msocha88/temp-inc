package io.kontak.apps.temperature.generator;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TemperatureGeneratorProperties.class)
public class TemperatureGeneratorConfig {

    @Bean
    TemperatureGenerator temperatureGenerator(TemperatureGeneratorProperties properties) {
        return new SimpleTemperatureGenerator(properties.getAnomalyRate(), properties.getThermometers());
    }

}
