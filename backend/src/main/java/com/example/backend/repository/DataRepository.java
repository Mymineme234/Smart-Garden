package com.example.backend.repository;

import com.example.backend.entity.SensorData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataRepository extends CrudRepository<SensorData, Integer> {
    Optional<SensorData> findByGardenIdAndSensorType(int gardenId, String sensorType);
}