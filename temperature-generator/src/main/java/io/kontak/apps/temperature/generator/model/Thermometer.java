package io.kontak.apps.temperature.generator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Thermometer {
    private String roomId;
    private String thermometerId;
}
