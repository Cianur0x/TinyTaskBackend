package org.iesvdm.preproyectoapirest.service;

import org.iesvdm.preproyectoapirest.domain.User;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    User saveImageFile(Long recipeId, MultipartFile file);
}
