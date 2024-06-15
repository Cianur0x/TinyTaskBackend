package org.iesvdm.preproyectoapirest.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.ImageUploadResponse;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.repository.UserRepository;
import org.iesvdm.preproyectoapirest.utils.ImageUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ImageServiceImpl {

    private final UserRepository userRepository;

    public ImageUploadResponse uploadImage(Long id, MultipartFile file) throws IOException {
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (!file.isEmpty() && optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setProfilePicture(ImageUtils.compressImage(file.getBytes()));
            userRepository.save(user);

            return new ImageUploadResponse("Image uploaded successfully: " + file.getOriginalFilename());
        }

        return new ImageUploadResponse("Image not uploaded: " + file.getOriginalFilename());
    }

    public byte[] getImage(Long id) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return ImageUtils.decompressImage(user.getProfilePicture());
        }

        return null;
    }

    public void delete(Long id) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setProfilePicture(null);
            userRepository.save(user);
        }
    }
}
