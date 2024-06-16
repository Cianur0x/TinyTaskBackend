package org.iesvdm.preproyectoapirest.mapper;

import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.dto.EditUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EditUserMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "biography", target = "bio")
    @Mapping(source = "state", target = "state")
    EditUserDTO userToUserDTO(User user);

    User editUserDTOTOUser(EditUserDTO editUserDTO);
}