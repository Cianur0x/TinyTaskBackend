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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
            return this.taskRepository.findTaskByTag_NameContainingIgnoreCase(findOpt.get(), sort);
        } else {
            log.info("OPCION 2");
            return sort != null ? this.taskRepository.findAll(sort) :
                    this.taskRepository.findAll();
        }
    }

    // FIXME no se deberia usar esto ya que se hace una llamada por día osea , 31 llamadas xdxd
    public List<Task> getTaskByDeadline(String deadline, Long userId) {
        List<Task> all;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date;

        try {
            date = formatter.parse(deadline);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        all = this.taskRepository.findTaskByDeadLineAndUser_IdOrderByTitleAsc(date, userId);

        return all;
    }

    public List<Task> getTaskByIsChecked(Boolean isChecked, Long tagId, Long userId) {
        return this.taskRepository.findTaskByTaskDoneAndUser_IdAndTag_IdOrderByTitleAsc(isChecked, tagId, userId);
    }

    public List<Task> getTaskByIsChecked(Boolean isChecked, Long userId) {
        Sort sort = Sort.by("deadLine").ascending() ;
        return this.taskRepository.findTaskByTaskDoneAndUser_Id(isChecked, userId, sort);
    }

    public List<Task> getAllTaskByTagId(Long tagId, Long userId) {
        List<Task> all = new ArrayList<>();

        all.addAll(this.taskRepository.findTaskByTaskDoneAndUser_IdAndTag_IdOrderByTitleAsc(false, tagId, userId));
        all.addAll(this.taskRepository.findTaskByTaskDoneAndUser_IdAndTag_IdOrderByTitleAsc(true, tagId, userId));

        return all;
    }

    public List<Task> getTaskByMonth(String startDate, String endDate, Long userId) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date start;
        Date end;
        try {
            start = formatter.parse(startDate);
            end = formatter.parse(endDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return this.taskRepository.getTasksByUserIDAndDeadlineBetween(start, end, userId);
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
        return this.taskRepository.findById(id)
                .map(p -> (id.equals(task.getId()) ? this.taskRepository.save(task) : null))
                .orElseThrow(() -> new EntityNotFoundException(id, Task.class));
    }

    public void delete(Long id) {
        this.taskRepository.findById(id).map(p -> {
            this.taskRepository.delete(p);
            return p;
        }).orElseThrow(() -> new EntityNotFoundException(id, Task.class));

    }
}
