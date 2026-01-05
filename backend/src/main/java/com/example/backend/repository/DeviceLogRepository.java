package com.example.backend.repository;

import com.example.backend.entity.DeviceLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceLogRepository extends CrudRepository<DeviceLog, Integer> {
}
