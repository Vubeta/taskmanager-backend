package com.devjourneyhub.taskmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.devjourneyhub.taskmanager.model.Task;

public interface TaskRepository extends JpaRepository<Task,Long> {
    Page<Task> findByCompleted(Boolean completed, Pageable pageable);
    Page<Task> findByTitleContainingIgnoreCase(String search, Pageable pageable);
}