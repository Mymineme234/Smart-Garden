package com.example.backend.entity;

import com.example.backend.constant.OnOffEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "device_log")
public class DeviceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH })
    private Device device;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OnOffEnum status;

    private LocalDateTime actionAt;

    // Constructors
    public DeviceLog() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Device getDevice() {
        return device;
    }
    public void setDevice(Device device) {
        this.device = device;
    }
    public OnOffEnum getStatus() {
        return status;
    }
    public void setStatus(OnOffEnum status) {
        this.status = status;
    }
    public LocalDateTime getActionAt() {
        return actionAt;
    }
    public void setActionAt(LocalDateTime actionAt) {
        this.actionAt = actionAt;
    }
}
