package com.example.backend.service;

import com.example.backend.constant.OnOffEnum;
import com.example.backend.dto.DeviceDTO;
import com.example.backend.entity.Device;
import com.example.backend.entity.Garden;
import com.example.backend.mqtt.MqttPublisher;
import com.example.backend.repository.DeviceLogRepository;
import com.example.backend.repository.DeviceRepository;
import com.example.backend.repository.GardenRepository;
import com.example.backend.repository.SchedulerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.backend.constant.OnOffEnum.OFF;
import static com.example.backend.constant.OnOffEnum.ON;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final MqttPublisher mqttPublisher;
    private final GardenRepository gardenRepository;
    private final SchedulerRepository schedulerRepository;
    private final DeviceLogRepository deviceLogRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, MqttPublisher mqttPublisher, GardenRepository gardenRepository, SchedulerRepository schedulerRepository, DeviceLogRepository deviceLogRepository){
        this.mqttPublisher=mqttPublisher;
        this.deviceRepository=deviceRepository;
        this.gardenRepository=gardenRepository;
        this.schedulerRepository=schedulerRepository;
        this.deviceLogRepository=deviceLogRepository;
    }

    private DeviceDTO convertToDTO(Device device){
        return new DeviceDTO(
                device.getId(),
                device.getDeviceName(),
                device.getStatus(),
                device.getGpio(),
                device.getGarden().getName()
        );
    }

    public List<DeviceDTO> getAllDevices(){
        List<Device> devices = (List<Device>) deviceRepository.findAll();
        return devices.stream()
                .map(this::convertToDTO)
                .sorted(java.util.Comparator.comparing(DeviceDTO::getId))
                .toList();
    }

    public DeviceDTO getDeviceDTOById(Integer id){
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));
        return convertToDTO(device);
    }

    @Transactional
    public DeviceDTO addDevice(String deviceName, int gpio, int gardenId) {
        if (deviceRepository.existsByDeviceName(deviceName)) {
            throw new IllegalArgumentException("Device name already exists");
        }

        Garden garden = gardenRepository.findById(gardenId)
                .orElseThrow(() -> new IllegalArgumentException("Garden not found"));

        Device device = new Device();
        device.setDeviceName(deviceName);
        device.setGpio(gpio);
        device.setGarden(garden);
        device.setStatus(OnOffEnum.OFF); // mặc định OFF

        deviceRepository.save(device);

        // Publish lên ESP32 để ESP32 add device mới
        String payload = "{ \"deviceId\": " + device.getId() + ", \"gpio\": " + gpio + " }";
        mqttPublisher.publish("new_device", payload);

        return convertToDTO(device);
    }

    public Device findDeviceById(Integer id){
        return deviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));
    }

    public void onDevice(Integer id){
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));

        // 🔧 FIX: payload thống nhất
        mqttPublisher.publish("control", device.getId() + "/ON");
    }

    public void offDevice(Integer id){
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));

        // 🔧 FIX: payload thống nhất
        mqttPublisher.publish("control", device.getId() + "/OFF");
    }

    @Transactional
    public void deleteDevice(int deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));

        schedulerRepository.deleteAll(device.getScheduler());

        deviceLogRepository.deleteAll(device.getDeviceLog());

        deviceRepository.delete(device);

        // Publish lên ESP32 để ESP32 xóa device
        String payload = "{ \"deviceId\": " + deviceId + " }";
        mqttPublisher.publish("delete_device", payload);
    }
}
