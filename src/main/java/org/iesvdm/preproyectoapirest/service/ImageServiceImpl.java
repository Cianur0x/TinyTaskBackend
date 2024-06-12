package org.iesvdm.preproyectoapirest.service;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final UserRepository userRepository;

    public ImageServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User saveImageFile(Long userId, MultipartFile file) {

        User user = null;

        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                user = optionalUser.get();
                Byte[] byteObjects = new Byte[file.getBytes().length];

                int i = 0;

                for (byte b : file.getBytes()) {
                    byteObjects[i++] = b;
                }

                user.setProfilePicture(byteObjects);

                userRepository.save(user);
                log.info("Imaged Saved!");
            }
        } catch (IOException e) {
            log.error("Error occurred", e);
        }

        return user;
    }
}
