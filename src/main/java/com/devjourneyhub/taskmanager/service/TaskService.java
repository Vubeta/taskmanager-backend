package com.devjourneyhub.taskmanager.service;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.devjourneyhub.taskmanager.model.Task;
import com.devjourneyhub.taskmanager.repository.TaskRepository;
import com.devjourneyhub.taskmanager.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Page<Task> getTasks(int page, int size, String sort, Boolean completed, String search) {
        String[] sortParts = sort.split(",");
        Sort.Direction direction = Sort.Direction.fromString(sortParts.length > 1 ? sortParts[1] : "desc");
        String sortBy = sortParts[0];
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        if (search != null && !search.isBlank()) {
            return taskRepository.findByTitleContainingIgnoreCase(search, pageable);
        } else if (completed != null) {
            return taskRepository.findByCompleted(completed, pageable);
        } else {
            return taskRepository.findAll(pageable);
        }
    }

    @Transactional
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Long id, Task taskDetails) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (taskDetails.getTitle() != null) {
            existingTask.setTitle(taskDetails.getTitle());
        }
        existingTask.setDescription(taskDetails.getDescription());
        existingTask.setCompleted(taskDetails.isCompleted());

        return taskRepository.save(existingTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found");
        }
        taskRepository.deleteById(id);
    }
}