package com.example.backend.dto;

import com.example.backend.constant.RepeatTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class SchedulerCreateRequest {
    private int deviceId;
    private RepeatTypeEnum repeatType;
    private LocalDateTime onTime;
    private LocalDateTime offTime;
}
