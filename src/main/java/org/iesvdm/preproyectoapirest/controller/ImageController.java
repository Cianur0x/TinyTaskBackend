package org.iesvdm.preproyectoapirest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.service.ImageService;
import org.iesvdm.preproyectoapirest.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/v1/api/image")
public class ImageController {
    private final ImageService imageService;
    private final UserService userService;

    public ImageController(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }

    @GetMapping("/{id}/image")
    public User showUploadForm(@PathVariable Long id, @RequestBody User user) {
        return this.userService.one(id);
    }

    @PostMapping("/{id}/image")
    public User handleImagePost(@PathVariable Long id, @RequestParam("imagefile") MultipartFile file) {
        return this.imageService.saveImageFile(id, file);
    }
}
