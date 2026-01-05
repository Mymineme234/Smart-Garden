package com.example.backend.service;

import com.example.backend.constant.OnOffEnum;
import com.example.backend.dto.DeviceLogDTO;
import com.example.backend.entity.Device;
import com.example.backend.entity.DeviceLog;
import com.example.backend.repository.DeviceLogRepository;
import com.example.backend.repository.DeviceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeviceLogService {

    private final DeviceLogRepository deviceLogRepository;
    private  final DeviceRepository deviceRepository;

    @Autowired
    public DeviceLogService(DeviceLogRepository deviceLogRepository, DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
        this.deviceLogRepository = deviceLogRepository;
    }

    public DeviceLogDTO convertToDTO(DeviceLog deviceLog){
        return new DeviceLogDTO(
                deviceLog.getId(),
                deviceLog.getDevice().getDeviceName(),
                deviceLog.getStatus(),
                deviceLog.getActionAt()
        );
    }

    public List<DeviceLogDTO> getAllDeviceLogs() {
        List<DeviceLog> deviceLogs = (List<DeviceLog>) deviceLogRepository.findAll();
        return deviceLogs.stream()
                .map(this::convertToDTO)
                .sorted(java.util.Comparator.comparing(DeviceLogDTO::getId))
                .toList();
    }

    // 🔧 FIX: gom update + log
    @Transactional
    public void updateDeviceStatus(int deviceId, OnOffEnum status, LocalDateTime time) {

        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));

        device.setStatus(status);
        deviceRepository.save(device);

        DeviceLog log = new DeviceLog();
        log.setDevice(device);
        log.setStatus(status);
        log.setActionAt(time);

        deviceLogRepository.save(log);
    }
}
