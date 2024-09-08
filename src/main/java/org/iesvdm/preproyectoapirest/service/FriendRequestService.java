package org.iesvdm.preproyectoapirest.service;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.*;
import org.iesvdm.preproyectoapirest.dto.RequestDTO;
import org.iesvdm.preproyectoapirest.exception.EntityNotFoundException;
import org.iesvdm.preproyectoapirest.mapper.RequestMapper;
import org.iesvdm.preproyectoapirest.repository.FriendRequestRepository;
import org.iesvdm.preproyectoapirest.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    public FriendRequestService(FriendRequestRepository friendRequestRepository, UserRepository userRepository, RequestMapper requestService) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
        this.requestMapper = requestService;
    }

    public List<FriendRequest> all() {
        return this.friendRequestRepository.findAll();
    }

    public List<RequestDTO> friendRequestsSend(Long userId) {
        List<FriendRequest> list = this.friendRequestRepository.findAllBySender_IdAndStatus(userId, Status.PENDING);
        List<RequestDTO> newListDTO = new ArrayList<>();
        if (!list.isEmpty()) {
            for (FriendRequest friendRequest : list) {
                RequestDTO requestDTO = requestMapper.requestToRequestDTO(friendRequest);
                requestDTO.setId(friendRequest.getId());
                newListDTO.add(requestDTO);
            }
            return newListDTO;
        }
        return null;
    }

    private boolean checkIfRequestExists(User sender, User receiver) {
        List<FriendRequest> friendRequest = this.friendRequestRepository.findAll();

        return friendRequest.stream().anyMatch(request -> request.getSender().equals(sender) && request.getReceiver().equals(receiver));
    }

    // todo cambiar la implementacion de amistad,que se añadan en ambos lados
    private boolean checkIfFriendshipExists(User sender, User receiver) {
        Set<User> senderFriendList = sender.getFriendList();
        Set<User> receiverFriendList = receiver.getFriendList();

        boolean exists = senderFriendList.stream().anyMatch(senderFriend -> senderFriend.equals(receiver));
        boolean exists2 = receiverFriendList.stream().anyMatch(receiverFriend -> receiverFriend.equals(sender));

        return exists && exists2;
    }


    // todo check if friendship already exists
    public ResponseEntity<?> save(RequestDTO friendRequest) {
        Optional<User> optSender = this.userRepository.findById(friendRequest.getSender());
        Optional<User> optReceiver = this.userRepository.findByUsername(friendRequest.getReceiver());

        if (optSender.isPresent() && optReceiver.isPresent()) {
            User sender = optSender.get();
            User receiver = optReceiver.get();
            if (!checkIfFriendshipExists(sender, receiver)) {
                if (checkIfRequestExists(receiver, sender)) { // si es reciproco osea esta condicion se udatea a accepted
                    // necesito el id de la friend request para devolverla y luego updatearla
                    Optional<FriendRequest> friendRequestToUpdate = this.friendRequestRepository.findBySender_IdAndReceiver_Id(receiver.getId(), sender.getId());
                    // la condicion es valida osea que si que hay uno que coincida paro cuando trato de recuperlo no va hmmm
                    if (friendRequestToUpdate.isPresent()) {
                        FriendRequest request = friendRequestToUpdate.get();
                        FriendRequest updatedRequest = new FriendRequest(sender, receiver);

                        updatedRequest.setStatus(Status.ACCEPTED);
                        return this.replace(request.getId(), updatedRequest);
                    }

                } else if (this.checkIfRequestExists(sender, receiver)) {   // is no existe tengo qu comborbar que no exista una peticion pendiente y si no existe la creo
                    Optional<FriendRequest> friendRequestToUpdate = this.friendRequestRepository.findBySender_IdAndReceiver_Id(sender.getId(), receiver.getId());
                    if (friendRequestToUpdate.isPresent()) {
                        FriendRequest request = friendRequestToUpdate.get();
                        FriendRequest updatedRequest = new FriendRequest(sender, receiver);
                        updatedRequest.setStatus(Status.ACCEPTED);

                        return this.replace(request.getId(), updatedRequest);
                    }
                } else {
                    FriendRequest friendRequestToSave = new FriendRequest(sender, receiver);
                    this.friendRequestRepository.save(friendRequestToSave);

                    return this.replace(friendRequestToSave.getId(), friendRequestToSave);
                }
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse("Friend already exists"));
            }
        }

        return ResponseEntity.badRequest().body(new MessageResponse("User not found"));
    }

    public FriendRequest one(Long id) {
        return this.friendRequestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, FriendRequest.class));
    }

    public ResponseEntity<?> replace(Long id, FriendRequest friendRequest) {
        Optional<FriendRequest> optFriendRequest = this.friendRequestRepository.findById(id);

        if (optFriendRequest.isPresent()) {
            FriendRequest requestToUpdate = optFriendRequest.get();

            if (requestToUpdate.getStatus().equals(Status.PENDING) && friendRequest.getStatus().equals(Status.ACCEPTED)) {
                User friend1 = requestToUpdate.getSender();
                User friend2 = requestToUpdate.getReceiver();

                friend1.getFriendList().add(friend2);
                friend2.getFriendList().add(friend1);

                this.userRepository.save(friend1);
                this.userRepository.save(friend2);

                requestToUpdate.setStatus(friendRequest.getStatus());
                this.friendRequestRepository.save(requestToUpdate);

                return ResponseEntity.ok(requestToUpdate);
            } else if (friendRequest.getStatus().equals(Status.DECLINED)) { // todo si esta declined y quiere volverse a enviar no podria??????
                requestToUpdate.setStatus(friendRequest.getStatus());
                this.friendRequestRepository.save(requestToUpdate);

                return ResponseEntity.ok(new MessageResponse("Friend Request declined"));
            } else if (requestToUpdate.getStatus().equals(Status.DECLINED)) {
                requestToUpdate.setStatus(Status.PENDING);
                this.friendRequestRepository.save(requestToUpdate);

                return ResponseEntity.ok(new MessageResponse("Friend Request pending"));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse("ola como he llegado aki"));
            }

        }

        return ResponseEntity.badRequest().body(new MessageResponse("Ha habido un error hehe"));
    }

    public void delete(Long id) {
        this.friendRequestRepository.findById(id).map(p -> {
            this.friendRequestRepository.delete(p);
            return p;
        }).orElseThrow(() -> new EntityNotFoundException(id, Theme.class));
    }
}
