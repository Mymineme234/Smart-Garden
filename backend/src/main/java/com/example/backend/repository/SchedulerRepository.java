package com.example.backend.repository;

import com.example.backend.entity.Scheduler;
import com.example.backend.constant.RepeatTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SchedulerRepository extends JpaRepository<Scheduler, Integer> {

    /**
     * 🔧 FIX:
     * - KHÔNG dùng fully-qualified enum
     * - So sánh enum bằng parameter
     */
    @Query("""
        SELECT s FROM Scheduler s
        WHERE s.enabled = true
          AND s.onTime <= :now
          AND (
              s.repeatType <> :onceType
              OR s.executed = false
          )
    """)
    List<Scheduler> findSchedulersToExecute(
            @Param("now") LocalDateTime now,
            @Param("onceType") RepeatTypeEnum onceType
    );
}
