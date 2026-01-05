package com.example.backend.controller;

import com.example.backend.dto.DeviceLogDTO;
import com.example.backend.service.DeviceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/device_logs")
public class DeviceLogController {

    private final DeviceLogService deviceLogService;

    @Autowired
    public DeviceLogController(DeviceLogService deviceLogService) {
        this.deviceLogService = deviceLogService;
    }

    @GetMapping
    public List<DeviceLogDTO> getAllDeviceLogs() {
        return deviceLogService.getAllDeviceLogs();
    }
}
