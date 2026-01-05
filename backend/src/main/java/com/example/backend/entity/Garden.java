package com.example.backend.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "garden")
public class Garden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "garden")
    private Set<SensorData> sensorData;

    @OneToMany(mappedBy = "garden")
    private Set<Device> device;

    // Constructors
    public Garden() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Set<SensorData> getSensorData() {
        return sensorData;
    }
    public void setSensorData(Set<SensorData> sensorData) {
        this.sensorData = sensorData;
    }
    public Set<Device> getDevice() {
        return device;
    }
    public void setDevice(Set<Device> device) {
        this.device = device;
    }
}
