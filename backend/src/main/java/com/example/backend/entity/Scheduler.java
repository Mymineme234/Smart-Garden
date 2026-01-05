package com.example.backend.entity;

import com.example.backend.constant.RepeatTypeEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "scheduler")
public class Scheduler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH })
    private Device device;

    private boolean enabled;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RepeatTypeEnum repeatType;

    private boolean executed;

    private LocalDateTime onTime;
    private LocalDateTime offTime;

    private LocalDateTime lastExecutedAt;

    // Constructors
    public Scheduler() {
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
    public LocalDateTime getOnTime() {
        return onTime;
    }
    public void setOnTime(LocalDateTime onTime) {
        this.onTime = onTime;
    }
    public LocalDateTime getOffTime() {
        return offTime;
    }
    public void setOffTime(LocalDateTime offTime) {
        this.offTime = offTime;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public RepeatTypeEnum getRepeatType() {
        return repeatType;
    }
    public void setRepeatType(RepeatTypeEnum repeatType) {
        this.repeatType = repeatType;
    }
    public boolean isExecuted() {
        return executed;
    }
    public void setExecuted(boolean executed) {
        this.executed = executed;
    }
    public LocalDateTime getLastExecutedAt() {
        return lastExecutedAt;
    }
    public void setLastExecutedAt(LocalDateTime lastExecutedAt) {
        this.lastExecutedAt = lastExecutedAt;
    }
}
