package com.absys.io.postgresql_mcp.repository;

import com.absys.io.postgresql_mcp.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(String status);

    List<Task> findByPriority(String priority);

    List<Task> findByAssignedTo(String assignedTo);
}
