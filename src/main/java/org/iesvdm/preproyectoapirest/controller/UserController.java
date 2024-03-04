package org.iesvdm.preproyectoapirest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
// @CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"", "/"})
    public List<User> all() {
        log.info("Accediendo a todos los usarios");
        return this.userService.all();
    }

    @PostMapping({"", "/"})
    public User newUser(@RequestBody User user) {
        return this.userService.save(user);
    }

    @GetMapping("/{id}")
    public User one(@PathVariable("id") Long id) {
        return this.userService.one(id);
    }

    @PutMapping("/{id}")
    public User replaceUser(@PathVariable("id") Long id, @RequestBody User user) {
         return this.userService.replace(id, user);

    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        this.userService.delete(id);
    }
}
