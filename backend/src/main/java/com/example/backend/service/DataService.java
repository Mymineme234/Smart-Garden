package com.example.backend.service;

import com.example.backend.dto.SensorDataDTO;
import com.example.backend.entity.SensorData;
import com.example.backend.repository.DataRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DataService {
    private final DataRepository dataRepository;

    @Autowired
    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public SensorDataDTO convertToDTO(SensorData sensorData) {
        return new SensorDataDTO(
                sensorData.getId(),
                sensorData.getSensorType(),
                sensorData.getGarden().getName(),
                sensorData.getValue(),
                sensorData.getUpdatedAt()
        );
    }

    public List<SensorDataDTO> getAllSensorData() {
        List<SensorData> sensorDatas = (List<SensorData>) dataRepository.findAll();
        return sensorDatas.stream()
                .map(this::convertToDTO)
                .sorted(java.util.Comparator.comparing(SensorDataDTO::getId))
                .toList();
    }

    // 🔧 FIX: update theo garden + sensorType
    public SensorData updateByGardenAndType(
            int gardenId,
            String sensorType,
            double value,
            LocalDateTime updatedAt
    ) {
        SensorData sensorData = dataRepository
                .findByGardenIdAndSensorType(gardenId, sensorType)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));

        sensorData.setValue(value);
        sensorData.setUpdatedAt(updatedAt);
        return dataRepository.save(sensorData);
    }
}
