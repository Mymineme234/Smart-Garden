package com.example.backend.repository;

import com.example.backend.entity.Device;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Integer> {
    Device findDeviceByDeviceName(String name);
    boolean existsByDeviceName(String name);
}
