package com.example.backend.controller;

import com.example.backend.dto.DeviceDTO;
import com.example.backend.entity.Device;
import com.example.backend.service.DeviceLogService;
import com.example.backend.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    public List<DeviceDTO> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/{id}")
    public DeviceDTO getDeviceById(@PathVariable Integer id) {
        return deviceService.getDeviceDTOById(id);
    }

    /**
     * 🔧 FIX:
     * - Controller CHỈ gửi lệnh MQTT
     * - KHÔNG tự ghi log
     * - KHÔNG tự cập nhật trạng thái
     */
    @GetMapping("/{id}/toggle")
    public String toggleDevice(@PathVariable Integer id) {

        Device device = deviceService.findDeviceById(id);

        if (device.getStatus() == com.example.backend.constant.OnOffEnum.ON) {
            deviceService.offDevice(id);
            return "OFF command sent to device";
        } else {
            deviceService.onDevice(id);
            return "ON command sent to device";
        }
    }

    /**
     * 🔧 FIX:
     * Endpoint bật thiết bị
     */
    @GetMapping("/{id}/on")
    public String turnOn(@PathVariable Integer id) {
        deviceService.onDevice(id);
        return "ON command sent to device";
    }

    /**
     * 🔧 FIX:
     * Endpoint tắt thiết bị
     */
    @GetMapping("/{id}/off")
    public String turnOff(@PathVariable Integer id) {
        deviceService.offDevice(id);
        return "OFF command sent to device";
    }

    // DTO cho request body thêm device
    public static class AddDeviceRequest {
        public String deviceName;
        public int gpio;
        public int gardenId;
    }

    @PostMapping
    public DeviceDTO addDevice(@RequestBody AddDeviceRequest request) {
        return deviceService.addDevice(request.deviceName, request.gpio, request.gardenId);
    }

    @DeleteMapping("/{id}")
    public String deleteDevice(@PathVariable int id) {
        deviceService.deleteDevice(id);
        return "Device deleted successfully";
    }
}
