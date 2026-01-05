package com.example.backend.controller;

import com.example.backend.entity.Garden;
import com.example.backend.service.GardenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gardens")
public class GardenController {

    private final GardenService gardenService;

    @Autowired
    public GardenController(GardenService gardenService){
        this.gardenService=gardenService;
    }

    @GetMapping
    public List<Garden> getAllGardens(){
        return gardenService.getAllGardens();
    }
}
