package com.example.backend.service;

import com.example.backend.entity.Garden;
import com.example.backend.repository.GardenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GardenService {

    private final GardenRepository gardenRepository;

    @Autowired
    public GardenService(GardenRepository gardenRepository) {
        this.gardenRepository = gardenRepository;
    }

    public List<Garden> getAllGardens() {
        List<Garden> gardens = (List<Garden>) gardenRepository.findAll();
        return gardens.stream()
                .sorted((java.util.Comparator.comparing(Garden::getId)))
                .toList();
    }

    public Garden findGardenById(int id) {
        return gardenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Garden not found with id: " + id));
    }
}
