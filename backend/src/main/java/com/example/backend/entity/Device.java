package com.example.backend.entity;

import com.example.backend.constant.OnOffEnum;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String deviceName;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH })
    private Garden garden;

    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<DeviceLog> deviceLog;

    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Scheduler> scheduler;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OnOffEnum status;

    @Column(nullable = false)
    private int gpio;

    // Constructors
    public Device() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDeviceName() {
        return deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public OnOffEnum getStatus() {
        return status;
    }
    public void setStatus(OnOffEnum status) {
        this.status = status;
    }
    public Garden getGarden() {
        return garden;
    }
    public void setGarden(Garden garden) {
        this.garden = garden;
    }
    public Set<DeviceLog> getDeviceLog() {
        return deviceLog;
    }
    public void setDeviceLog(Set<DeviceLog> deviceLog) {
        this.deviceLog = deviceLog;
    }
    public Set<Scheduler> getScheduler() {
        return scheduler;
    }
    public void setScheduler(Set<Scheduler> scheduler) {
        this.scheduler = scheduler;
    }
    public int getGpio() {
        return gpio;
    }
    public void setGpio(int gpio) {
        this.gpio = gpio;
    }
}
