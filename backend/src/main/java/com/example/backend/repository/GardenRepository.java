package com.example.backend.repository;

import com.example.backend.entity.Garden;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GardenRepository extends CrudRepository<Garden, Integer> {
    boolean existsByName(String name);
    void deleteById(Integer id);
    Garden findByName(String name);
}
