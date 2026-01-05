package com.example.backend.dto;

import com.example.backend.constant.RepeatTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class            SchedulerDTO implements Serializable {
    private final int id;
    private final int DeviceId; // 🔧 FIX: String
    private final RepeatTypeEnum repeatType;
    private final LocalDateTime onTime;
    private final LocalDateTime offTime;
}
