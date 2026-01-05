package com.example.backend.controller;

import com.example.backend.dto.SensorDataDTO;
import com.example.backend.entity.SensorData;
import com.example.backend.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensor_datas")
public class DataController {
    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping
    public List<SensorDataDTO> getAllSensorData() {
        return dataService.getAllSensorData();
    }
}
