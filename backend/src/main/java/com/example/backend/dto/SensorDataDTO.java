package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class SensorDataDTO implements Serializable {
    private final int id;
    private final String sensorType;
    private final String gardenName;
    private final double value;
    private final LocalDateTime updatedAt;
}