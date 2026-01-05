package com.example.backend.dto;

import com.example.backend.constant.OnOffEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class DeviceLogDTO implements Serializable {
    private final int id;
    private final String deviceName;
    private final OnOffEnum status;
    private final LocalDateTime actionAt;
}