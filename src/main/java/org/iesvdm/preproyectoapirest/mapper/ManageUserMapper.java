package org.iesvdm.preproyectoapirest.mapper;

import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.dto.ManageUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ManageUserMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "lastConnection", target = "lastConnection", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "roles", target = "rol")
    ManageUserDTO userToManUserDTO(User user);

    User manUserDTOTOUser(ManageUserDTO userDTO);
}
