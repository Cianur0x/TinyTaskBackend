package org.iesvdm.preproyectoapirest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.Role;
import org.iesvdm.preproyectoapirest.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
// @CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/v1/api/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(value = {"", "/"}, params = {"!search", "!order", "!page", "!size"})
    public List<Role> all() {
        log.info("Accediendo a todos los roles");
        return this.roleService.all();
    }

    @GetMapping(value = {"", "/"}, params = {"!page", "!size"})
    public List<Role> all(@RequestParam("search") Optional<String> findOpt,
                          @RequestParam("order") Optional<String> orderOpt) {
        log.info("Accediendo a todos los roles con filtros");
        return this.roleService.all(findOpt, orderOpt);
    }

    @GetMapping(value = {"", "/"})
    public ResponseEntity<Map<String, Object>> all(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "2") int size) {
        log.info("Accediendo a roles con paginación");
        Map<String, Object> responseAll = this.roleService.all(page, size);
        return ResponseEntity.ok(responseAll);
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
