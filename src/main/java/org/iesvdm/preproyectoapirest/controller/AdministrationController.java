package org.iesvdm.preproyectoapirest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.dto.ManageUserDTO;
import org.iesvdm.preproyectoapirest.dto.UserRoleDTO;
import org.iesvdm.preproyectoapirest.service.AdministrationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("https://tiny-task-v1.vercel.app")
@RequestMapping("/v1/api/admin")
public class AdministrationController {

    private final AdministrationService administrationService;

    public AdministrationController(AdministrationService administrationService) {
        this.administrationService = administrationService;
    }

    @GetMapping(value = {"", "/"})
    public List<ManageUserDTO> all() {
        log.info("Accediendo a todos los usuarios ManageUserDTO");
        return this.administrationService.getAllUsers();
    }

    @PutMapping("/editrole")
    public ManageUserDTO editUser(@RequestBody UserRoleDTO userUpdate) {
        return this.administrationService.editUser(userUpdate);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        this.administrationService.delete(id);
    }
}
