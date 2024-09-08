package org.iesvdm.preproyectoapirest.mapper;

import org.iesvdm.preproyectoapirest.domain.FriendRequest;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.dto.RequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(source = "sender.id", target = "sender")
    @Mapping(source = "receiver.username", target = "receiver")
    @Mapping(source = "status", target = "status")
    RequestDTO requestToRequestDTO(FriendRequest user);

    FriendRequest requestDTOtoUser(RequestDTO userDTO);


    // Custom method to convert Long to User
    default User mapSender(Long senderId) {
        if (senderId == null) {
            return null;
        }
        User user = new User();
        user.setId(senderId);
        return user;
    }

    // Custom method to map receiver username to User (if you fetch by username)
    default User mapReceiver(String receiverUsername) {
        if (receiverUsername == null) {
            return null;
        }
        User user = new User();
        user.setUsername(receiverUsername); // Assuming you only need to set the username for now
        return user;
    }
}
