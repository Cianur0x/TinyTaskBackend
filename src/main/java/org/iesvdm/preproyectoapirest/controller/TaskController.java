package org.iesvdm.preproyectoapirest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.Task;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.service.TaskService;
import org.iesvdm.preproyectoapirest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
// @CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(value = {"", "/"})
    public List<Task> all() {
        log.info("Accediendo a todos los tareas");
        return this.taskService.all();
    }

    @PostMapping({"", "/"})
    public Task newTask(@RequestBody Task user) {
        return this.taskService.save(user);
    }

    @GetMapping("/{id}")
    public Task one(@PathVariable("id") Long id) {
        return this.taskService.one(id);
    }

    @PutMapping("/{id}")
    public Task replaceTask(@PathVariable("id") Long id, @RequestBody Task task) {
        return this.taskService.replace(id, task);

    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") Long id) {
        this.taskService.delete(id);
    }
}
