package com.devjourneyhub.taskmanager.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import lombok.RequiredArgsConstructor;
import com.devjourneyhub.taskmanager.model.Task;
import com.devjourneyhub.taskmanager.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "${app.cors.allowed-origins:*}") // config via env
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public Page<Task> getTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) String search
    ) {
        return taskService.getTasks(page, size, sort, completed, search);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
