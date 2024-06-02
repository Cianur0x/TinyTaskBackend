package org.iesvdm.preproyectoapirest.service;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.dto.UserDTO;
import org.iesvdm.preproyectoapirest.exception.EntityNotFoundException;
import org.iesvdm.preproyectoapirest.mapper.UserMapper;
import org.iesvdm.preproyectoapirest.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<User> all() {
        return this.userRepository.findAll();
    }

    public List<User> all(Optional<String> findOpt, Optional<String> orderOpt) {
        Sort sort = null;

        if (orderOpt.isPresent()) {
            sort = orderOpt.get().equals("desc") ? Sort.by("username").descending() : Sort.by("username").ascending();
        }

        if (findOpt.isPresent()) {
            return this.userRepository.findUsersByUsernameContainingIgnoreCase(findOpt.get(), sort);
        } else {
            return sort != null ? this.userRepository.findAll(sort) :
                    this.userRepository.findAll();
        }
    }

    public User findByUsername(Optional<String> findOpt) {
        List<User> justOne = this.all(findOpt, Optional.empty());
        if (!justOne.isEmpty()) {
            return justOne.getFirst();
        }
        return null;
    }

    public UserDTO addUserToFriendList(Optional<String> findOpt, Long idUser) {
        User userToFriend = this.findByUsername(findOpt);
        User currentUser = this.one(idUser);

        if (!currentUser.equals(userToFriend)) {
            Set<User> setFriends = currentUser.getFriendList();

            boolean friendExists = currentUser.getFriendList().stream().anyMatch(user -> user.getUsername().equals(userToFriend.getUsername()));
            if (!friendExists) {
                setFriends.add(userToFriend);
                this.userRepository.save(currentUser);

                return userMapper.userToUserDTO(userToFriend);
            }
        }

        return null;
    }

    public List<UserDTO> getFriendList(Long idUser) {
        User currentUser = this.one(idUser);
        List<UserDTO> userDTOs = new ArrayList<>();
        if (currentUser != null) {
            currentUser.getFriendList().forEach(user -> {
                UserDTO userDTO = userMapper.userToUserDTO(user);
                userDTOs.add(userDTO);
            });
        }
        return userDTOs;
    }

    public Map<String, Object> all(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
        Page<User> pageAll = this.userRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();

        response.put("users", pageAll.getContent());
        response.put("currentPage", pageAll.getNumber());
        response.put("totalItems", pageAll.getTotalElements());
        response.put("totalPages", pageAll.getTotalPages());

        return response;
    }

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public User one(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, User.class));
    }

    public User replace(Long id, User user) {
        return this.userRepository.findById(id).map(p -> (id.equals(user.getId()) ?
                        this.userRepository.save(user) : null))
                .orElseThrow(() -> new EntityNotFoundException(id, User.class));
    }

    public void delete(Long id) {
        this.userRepository.findById(id).map(p -> {
            this.userRepository.delete(p);
            return p;
        }).orElseThrow(() -> new EntityNotFoundException(id, User.class));
    }
}
