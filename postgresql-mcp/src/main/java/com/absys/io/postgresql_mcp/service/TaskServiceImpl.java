package com.absys.io.postgresql_mcp.service;

import com.absys.io.postgresql_mcp.entity.Task;
import com.absys.io.postgresql_mcp.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status);
    }

    @Override
    public List<Task> getTasksByPriority(String priority) {
        return taskRepository.findByPriority(priority);
    }

    @Override
    public Task updateTask(Long id, Task updatedTask) {

        return taskRepository.findById(id)
                .map(task -> {

                    if (updatedTask.getTitle() != null) {
                        task.setTitle(updatedTask.getTitle());
                    }

                    if (updatedTask.getDescription() != null) {
                        task.setDescription(updatedTask.getDescription());
                    }

                    if (updatedTask.getStatus() != null) {
                        task.setStatus(updatedTask.getStatus());
                    }

                    if (updatedTask.getPriority() != null) {
                        task.setPriority(updatedTask.getPriority());
                    }

                    if (updatedTask.getAssignedTo() != null) {
                        task.setAssignedTo(updatedTask.getAssignedTo());
                    }

                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

}
