package org.iesvdm.preproyectoapirest.service;

import org.iesvdm.preproyectoapirest.domain.Task;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.exception.EntityNotFoundException;
import org.iesvdm.preproyectoapirest.repository.TaskRepository;
import org.iesvdm.preproyectoapirest.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> all() {
        return this.taskRepository.findAll();
    }

    public Map<String, Object> paginado(String[] paginado) {
        int pagina = Integer.parseInt(paginado[0]);
        int tamanio = Integer.parseInt(paginado[1]);

        Pageable paginacion = PageRequest.of(pagina, tamanio, Sort.by("id").ascending());
        Page<Task> pageAll = this.taskRepository.findAll(paginacion);

        Map<String, Object> response = new HashMap<>();

        response.put("user", pageAll.getContent());
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
