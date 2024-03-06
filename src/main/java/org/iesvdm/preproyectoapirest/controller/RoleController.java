package org.iesvdm.preproyectoapirest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.Role;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.service.RoleService;
import org.iesvdm.preproyectoapirest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
// @CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/v1/api/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(value = {"", "/"})
    public List<Role> all() {
        log.info("Accediendo a todos los roles");
        return this.roleService.all();
    }

    @PostMapping({"", "/"})
    public Role newRole(@RequestBody Role role) {
        return this.roleService.save(role);
    }

    @GetMapping("/{id}")
    public Role one(@PathVariable("id") Long id) {
        return this.roleService.one(id);
    }

    @PutMapping("/{id}")
    public Role replaceRole(@PathVariable("id") Long id, @RequestBody Role role) {
         return this.roleService.replace(id, role);

    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable("id") Long id) {
        this.roleService.delete(id);
    }
}
