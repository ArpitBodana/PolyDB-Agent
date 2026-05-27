package com.absys.io.postgresql_mcp.service;

import com.absys.io.postgresql_mcp.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Task createTask(Task task);

    List<Task> getAllTasks();

    Optional<Task> getTaskById(Long id);

    List<Task> getTasksByStatus(String status);

    List<Task> getTasksByPriority(String priority);

    Task updateTask(Long id, Task updatedTask);

    void deleteTask(Long id);
}