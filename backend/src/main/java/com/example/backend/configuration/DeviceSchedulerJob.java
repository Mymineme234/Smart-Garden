package com.example.backend.configuration;

import com.example.backend.constant.RepeatTypeEnum;
import com.example.backend.entity.Scheduler;
import com.example.backend.repository.SchedulerRepository;
import com.example.backend.service.SchedulerService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
public class DeviceSchedulerJob {

    private final SchedulerRepository schedulerRepository;
    private final SchedulerService schedulerService;

    public DeviceSchedulerJob(
            SchedulerRepository schedulerRepository,
            SchedulerService schedulerService
    ) {
        this.schedulerRepository = schedulerRepository;
        this.schedulerService = schedulerService;
    }

    @Scheduled(fixedRate = 1_000) // mỗi 30 giây
    public void run() {
        LocalDateTime now = LocalDateTime.now();
        List<Scheduler> schedulers =
                schedulerRepository.findSchedulersToExecute(
                        now,
                        RepeatTypeEnum.ONCE
                );


        for (Scheduler s : schedulers) {
            schedulerService.execute(s);
        }
    }
}