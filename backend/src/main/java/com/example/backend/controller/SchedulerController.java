package com.example.backend.controller;

import com.example.backend.constant.RepeatTypeEnum;
import com.example.backend.dto.DeviceDTO;
import com.example.backend.dto.SchedulerCreateRequest;
import com.example.backend.dto.SchedulerDTO;
import com.example.backend.entity.Device;
import com.example.backend.entity.Scheduler;
import com.example.backend.repository.DeviceRepository;
import com.example.backend.repository.SchedulerRepository;
import com.example.backend.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

    @RestController
    @RequestMapping("/api/schedulers")
    public class SchedulerController {

        private final SchedulerRepository schedulerRepository;
        private final DeviceRepository deviceRepository;
        private final SchedulerService schedulerService;

        @Autowired
        public SchedulerController(
                SchedulerRepository schedulerRepository,
                DeviceRepository deviceRepository,
                SchedulerService schedulerService
        ) {
            this.schedulerRepository = schedulerRepository;
            this.deviceRepository = deviceRepository;
            this.schedulerService = schedulerService;
        }

        /**
         * 🔧 FIX:
         * Tạo lịch bật/tắt thiết bị
         */
        @PostMapping
        public SchedulerDTO createScheduler(@RequestBody SchedulerCreateRequest request) {

            Device device = deviceRepository.findById(request.getDeviceId())
                    .orElseThrow(() -> new IllegalArgumentException("Device not found"));

            Scheduler scheduler = new Scheduler();
            scheduler.setDevice(device);
            scheduler.setRepeatType(request.getRepeatType());
            scheduler.setOnTime(request.getOnTime());
            scheduler.setOffTime(request.getOffTime());
            scheduler.setEnabled(true);
            scheduler.setExecuted(false);

            schedulerRepository.save(scheduler);
            return schedulerService.convertToDTO(scheduler);
        }


        /**
         * 🔧 FIX:
         * Lấy toàn bộ lịch
         */
        @GetMapping
        public List<SchedulerDTO> getAllSchedulers() {
            return schedulerService.getAllSchedulers();
        }

        /**
         * 🔧 FIX:
         * Bật / tắt lịch
         */
        @PutMapping("/{id}/enable")
        public Scheduler enableScheduler(@PathVariable int id) {
            Scheduler scheduler = schedulerRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Scheduler not found"));
            scheduler.setEnabled(true);
            return schedulerRepository.save(scheduler);
        }

        @PutMapping("/{id}/disable")
        public Scheduler disableScheduler(@PathVariable int id) {
            Scheduler scheduler = schedulerRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Scheduler not found"));
            scheduler.setEnabled(false);
            return schedulerRepository.save(scheduler);
        }

        /**
         * 🔧 FIX:
         * Xóa lịch
         */
        @DeleteMapping("/{id}")
        public void deleteScheduler(@PathVariable int id) {
            schedulerRepository.deleteById(id);
        }
    }
