package io.kontakt.apps.temperature.generator;

import io.kontak.apps.event.TemperatureReading;
import io.kontak.apps.temperature.generator.SimpleTemperatureGenerator;
import io.kontak.apps.temperature.generator.model.Thermometer;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleTemperatureGeneratorTest {

    @Test
    public void shouldGenerateTemperatureReading() {
        //given
        var thermometers = List.of(
                new Thermometer("hall", "thermometer1"),
                new Thermometer("hall", "thermometer2"));

        var tested = new SimpleTemperatureGenerator(0.1, thermometers);

        //when
        Collection<TemperatureReading> result = tested.generate();

        //then
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(thermometers.size());
    }
}
