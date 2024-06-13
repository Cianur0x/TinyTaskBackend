package org.iesvdm.preproyectoapirest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.service.ImageService;
import org.iesvdm.preproyectoapirest.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@CrossOrigin(origins = "https://tiny-task-v1.vercel.app")
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

    @Transactional
    @GetMapping("/{id}/userimage")
    public ResponseEntity<?> renderImageFromDB(@PathVariable Long id) throws IOException {
        User recipeCommand = this.userService.one(id);

        if (recipeCommand.getProfilePicture() != null) {
            byte[] byteArray = new byte[recipeCommand.getProfilePicture().length];
            int i = 0;

            for (Byte wrappedByte : recipeCommand.getProfilePicture()) {
                byteArray[i++] = wrappedByte; // auto unboxing
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(byteArray, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Image not found", HttpStatus.NOT_FOUND);
        }
    }

}
