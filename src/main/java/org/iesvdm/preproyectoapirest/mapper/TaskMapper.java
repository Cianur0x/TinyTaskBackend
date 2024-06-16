package org.iesvdm.preproyectoapirest.mapper;

import org.iesvdm.preproyectoapirest.domain.Task;
import org.iesvdm.preproyectoapirest.dto.TaskDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "user.username", target = "owner")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "taskDone", target = "taskDone")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "deadLine", target = "deadLine", dateFormat = "yyyy-MM-dd")
    TaskDTO userToManUserDTO(Task user);

    Task manUserDTOTOUser(TaskDTO userDTO);
}
