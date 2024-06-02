package org.iesvdm.preproyectoapirest.mapper;

import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "imgProfile", target = "imgProfile")
    @Mapping(source = "state", target = "state")
    UserDTO userToUserDTO(User user);

    User userDTOAUser(UserDTO clienteDTO);
}
