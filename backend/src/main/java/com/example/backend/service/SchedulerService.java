package com.example.backend.service;

import com.example.backend.dto.SchedulerDTO;
import com.example.backend.repository.SchedulerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.example.backend.entity.Scheduler;
import com.example.backend.constant.RepeatTypeEnum;
import com.example.backend.repository.SchedulerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SchedulerService {

    private final SchedulerRepository schedulerRepository;
    private final DeviceService deviceService;

    public SchedulerService(
            SchedulerRepository schedulerRepository,
            DeviceService deviceService
    ) {
        this.schedulerRepository = schedulerRepository;
        this.deviceService = deviceService;
    }

    public SchedulerDTO convertToDTO(Scheduler scheduler){
        return new SchedulerDTO(
                scheduler.getId(),
                scheduler.getDevice().getId(),
                scheduler.getRepeatType(),
                scheduler.getOnTime(),
                scheduler.getOffTime()
        );
    }

    public List<SchedulerDTO> getAllSchedulers() {
        List<Scheduler> schedulers = (List<Scheduler>) schedulerRepository.findAll();
        return schedulers.stream()
                .map(this::convertToDTO)
                .sorted(java.util.Comparator.comparing(SchedulerDTO::getId))
                .toList();
    }

    @Transactional
    public void execute(Scheduler scheduler) {

        if (!scheduler.isEnabled()) return;

        LocalDateTime now = LocalDateTime.now();

        // 🔧 FIX: bật đúng onTime
        if (scheduler.getOnTime() != null &&
                now.isAfter(scheduler.getOnTime()) &&
                (scheduler.getLastExecutedAt() == null ||
                        scheduler.getLastExecutedAt().isBefore(scheduler.getOnTime()))) {

            deviceService.onDevice(scheduler.getDevice().getId());
            scheduler.setLastExecutedAt(now);
        }

        // 🔧 FIX: tắt đúng offTime
        if (scheduler.getOffTime() != null && now.isAfter(scheduler.getOffTime())) {
            deviceService.offDevice(scheduler.getDevice().getId());

            if (scheduler.getRepeatType() == RepeatTypeEnum.ONCE) {
                scheduler.setEnabled(false);
                scheduler.setExecuted(true);
            }
        }

        schedulerRepository.save(scheduler);
    }
}
