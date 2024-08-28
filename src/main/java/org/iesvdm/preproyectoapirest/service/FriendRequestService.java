package org.iesvdm.preproyectoapirest.service;

import org.iesvdm.preproyectoapirest.domain.FriendRequest;
import org.iesvdm.preproyectoapirest.domain.Theme;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.dto.RequestDTO;
import org.iesvdm.preproyectoapirest.exception.EntityNotFoundException;
import org.iesvdm.preproyectoapirest.repository.FriendRequestRepository;
import org.iesvdm.preproyectoapirest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    public FriendRequestService(FriendRequestRepository friendRequestRepository, UserRepository userRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
    }

    public List<FriendRequest> all() {
        return this.friendRequestRepository.findAll();
    }

    public FriendRequest save(RequestDTO friendRequest) {
        Optional<User> optSender = this.userRepository.findById(friendRequest.getSender());
        Optional<User> optReceiver = this.userRepository.findByUsername(friendRequest.getReceiver());
        if (optSender.isPresent() && optReceiver.isPresent()) {
            User sender = optSender.get();
            User receiver = optReceiver.get();
            FriendRequest friendRequestToSave = new FriendRequest(sender, receiver);

            return //   todo devolver una fr normal, y parsearla luego con sender, receiver y status
                    this.friendRequestRepository.save(friendRequestToSave);
        }

        return null;
    }

    public FriendRequest one(Long id) {
        return this.friendRequestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Theme.class));
    }

    public FriendRequest replace(Long id, FriendRequest friendRequest) {
        return this.friendRequestRepository.findById(id).map(p -> (id.equals(friendRequest.getId()) ? this.friendRequestRepository.save(friendRequest) : null))
                .orElseThrow(() -> new EntityNotFoundException(id, FriendRequest.class));
    }

    public void delete(Long id) {
        this.friendRequestRepository.findById(id).map(p -> {
            this.friendRequestRepository.delete(p);
            return p;
        }).orElseThrow(() -> new EntityNotFoundException(id, Theme.class));
    }
}
