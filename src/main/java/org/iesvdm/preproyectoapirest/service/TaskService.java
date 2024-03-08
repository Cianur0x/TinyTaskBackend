package org.iesvdm.preproyectoapirest.service;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.Task;
import org.iesvdm.preproyectoapirest.exception.EntityNotFoundException;
import org.iesvdm.preproyectoapirest.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> all() {
        return this.taskRepository.findAll();
    }

    public List<Task> all(Optional<String> findOpt, Optional<String> orderOpt) {
        Sort sort = null;

        if (orderOpt.isPresent()) {
            sort = orderOpt.get().equals("desc") ? Sort.by("title").descending() : Sort.by("title").ascending();
        }

        if (findOpt.isPresent()) {
            log.info("OPCION 1");
            //return this.taskRepository.findTaskByTagName(findOpt.get(), sort);
            return this.taskRepository.findTaskByTag_NameContainingIgnoreCase(findOpt.get(), sort);
        } else {
            log.info("OPCION 2");
            return sort != null ? this.taskRepository.findAll(sort) :
                    this.taskRepository.findAll();
        }
    }

    public Map<String, Object> all(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("tag_name").ascending());
        Page<Task> pageAll = this.taskRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();

        response.put("tasks", pageAll.getContent());
        response.put("currentPage", pageAll.getNumber());
        response.put("totalItems", pageAll.getTotalElements());
        response.put("totalPages", pageAll.getTotalPages());

        return response;
    }

    public Task save(Task task) {
        return this.taskRepository.save(task);
    }

    public Task one(Long id) {
        return this.taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Task.class));
    }

    public Task replace(Long id, Task task) {
        return this.taskRepository.findById(id).map(p -> (id.equals(task.getId()) ? this.taskRepository.save(task) : null)).orElseThrow(() -> new EntityNotFoundException(id, Task.class));
    }

    public void delete(Long id) {
        this.taskRepository.findById(id).map(p -> {
            this.taskRepository.delete(p);
            return p;
        }).orElseThrow(() -> new EntityNotFoundException(id, Task.class));

    }
}
