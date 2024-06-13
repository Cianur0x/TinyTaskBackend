package org.iesvdm.preproyectoapirest.controller;

import org.iesvdm.preproyectoapirest.domain.MessageResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("https://tiny-task-v1.vercel.app")
@RequestMapping("/v1/api/prueba")
public class PruebaController {

    @GetMapping({"", "/"})
    public MessageResponse prueba() {
        return new MessageResponse("Prueba security con éxito");
    }

    @GetMapping("/solo-admin")
    public MessageResponse pruebaSoloAdmin() {
        return new MessageResponse("Prueba security solo-admin con éxito");
    }

}
