package com.absys.io.postgresql_mcp.tools;

import com.absys.io.postgresql_mcp.entity.Task;
import com.absys.io.postgresql_mcp.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskTools {

    private final TaskService taskService;

    @Tool(
            name = "create_task",
            description = "Create a new task with title, description, priority, assignedTo, and status"
    )
    public Task createTask(  String title,
                             String description,
                             String priority,
                             String assignedTo,
                             String status) {

        log.info("[create_task] Request received: title={}, priority={}, assignedTo={}",
                title, priority, assignedTo);

        Task task = Task.builder()
                .title(title)
                .description(description)
                .priority(priority)
                .assignedTo(assignedTo)
                .status(status)
                .build();

        Task created = taskService.createTask(task);

        log.info("[create_task] Task created successfully with id={}", created.getId());

        return created;
    }

    @Tool(
            name = "get_all_tasks",
            description = "Fetch all tasks from database"
    )
    public List<Task> getAllTasks() {

        log.info("[get_all_tasks] Fetching all tasks");

        List<Task> tasks = taskService.getAllTasks();

        log.info("[get_all_tasks] Total tasks found: {}", tasks.size());

        return tasks;
    }

    @Tool(
            name = "get_task_by_id",
            description = "Get task by ID"
    )
    public Task getTaskById(Long id) {

        log.info("[get_task_by_id] Request for id={}", id);

        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> {
                    log.error("[get_task_by_id] Task not found for id={}", id);
                    return new RuntimeException("Task not found");
                });

        log.info("[get_task_by_id] Task found: title={}", task.getTitle());

        return task;
    }

    @Tool(
            name = "get_tasks_by_status",
            description = "Get tasks filtered by status"
    )
    public List<Task> getTasksByStatus(String status) {

        log.info("[get_tasks_by_status] status={}", status);

        List<Task> tasks = taskService.getTasksByStatus(status);

        log.info("[get_tasks_by_status] Found {} tasks", tasks.size());

        return tasks;
    }

    @Tool(
            name = "get_tasks_by_priority",
            description = "Get tasks filtered by priority"
    )
    public List<Task> getTasksByPriority(String priority) {

        log.info("[get_tasks_by_priority] priority={}", priority);

        List<Task> tasks = taskService.getTasksByPriority(priority);

        log.info("[get_tasks_by_priority] Found {} tasks", tasks.size());

        return tasks;
    }

    @Tool(
            name = "update_task",
            description = "Update task by ID"
    )
    public Task updateTask(Long id, Task updatedTask) {

        log.info("[update_task] Updating task id={}", id);

        Task task = taskService.updateTask(id, updatedTask);

        log.info("[update_task] Task updated successfully id={}", task.getId());

        return task;
    }

    @Tool(
            name = "delete_task",
            description = "Delete task by ID"
    )
    public String deleteTask(Long id) {

        log.info("[delete_task] Deleting task id={}", id);

        taskService.deleteTask(id);

        log.info("[delete_task] Task deleted successfully id={}", id);

        return "Task deleted successfully";
    }
}