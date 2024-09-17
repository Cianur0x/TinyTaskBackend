package org.iesvdm.preproyectoapirest.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.iesvdm.preproyectoapirest.domain.FriendRequest;
import org.iesvdm.preproyectoapirest.domain.MessageResponse;
import org.iesvdm.preproyectoapirest.domain.Task;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.dto.EditUserDTO;
import org.iesvdm.preproyectoapirest.dto.UserDTO;
import org.iesvdm.preproyectoapirest.exception.EntityNotFoundException;
import org.iesvdm.preproyectoapirest.mapper.EditUserMapper;
import org.iesvdm.preproyectoapirest.mapper.UserMapper;
import org.iesvdm.preproyectoapirest.repository.FriendRequestRepository;
import org.iesvdm.preproyectoapirest.repository.TaskRepository;
import org.iesvdm.preproyectoapirest.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final EditUserMapper editUserMapper;
    private final FriendRequestRepository friendRequestRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, TaskRepository taskRepository, FriendRequestRepository friendRequestRepository, UserMapper userMapper, EditUserMapper editUserMapper, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.editUserMapper = editUserMapper;
        this.userMapper = userMapper;
        this.encoder = encoder;
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

    public ResponseEntity<?> addUserToFriendList(Optional<String> findOpt, Long idUser) {
        if (findOpt.isPresent()) {
            Optional<User> optionalUser = this.userRepository.findByUsername(findOpt.get());

            if (optionalUser.isPresent()) {
                User currentUser = this.one(idUser);
                User userToFriend = optionalUser.get();

                if (!currentUser.equals(userToFriend)) {
                    Set<User> setFriends = currentUser.getFriendList();

                    boolean friendExists = currentUser.getFriendList().stream().anyMatch(user -> user.getUsername().equals(userToFriend.getUsername()));
                    if (!friendExists) {
                        setFriends.add(userToFriend);
                        this.userRepository.save(currentUser);

                        UserDTO userDTO = userMapper.userToUserDTO(userToFriend);
                        return ResponseEntity.ok(userDTO);
                    }

                    return ResponseEntity.badRequest().body(new MessageResponse("Friend already exists"));
                }
            }
        }

        return ResponseEntity.badRequest().body(new MessageResponse("User not found"));
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

    public EditUserDTO getEditUser(Long id) {
        User currentUser = this.one(id);

        if (currentUser != null) {
            return editUserMapper.userToUserDTO(currentUser);
        }

        return null;
    }

    public User replace(Long id, User user) {
        return this.userRepository.findById(id).map(p -> (id.equals(user.getId()) ?
                        this.userRepository.save(user) : null))
                .orElseThrow(() -> new EntityNotFoundException(id, User.class));
    }

    public void delete(Long id) {
        this.userRepository.findById(id).map(p -> {

            p.getViewedTasks().forEach(task -> {
                task.getViewers().remove(p);
                this.taskRepository.save(task);
            });

            Set<User> users = this.userRepository.findFriendsByUserIdOrFriendId(id);
            users.forEach(user -> {
                user.getFriendList().remove(p);
                this.userRepository.save(user);
            });

            p.getTareasCreadas().forEach(task -> {
                task.getViewers().forEach(user -> {
                    user.getViewedTasks().remove(task);
                    this.userRepository.save(user);
                });
                this.taskRepository.save(task);
            });

            this.userRepository.delete(p);
            return p;
        }).orElseThrow(() -> new EntityNotFoundException(id, User.class));
    }

    public void deleteFriend(Long myId, Long friend) {
        Optional<User> optionalMyUser = this.userRepository.findById(myId);
        Optional<User> optionalMyFriend = this.userRepository.findById(friend);

        List<Task> tasks = new ArrayList<>();
        // todo buscar la solicud de amistad que quiero borrr para cambiar el estado a declined para que luego pueda volver a enviar una solicitus y se apruebe heheh

        if (optionalMyUser.isPresent() && optionalMyFriend.isPresent()) {
            User myUser = optionalMyUser.get();
            User friendUser = optionalMyFriend.get();
            myUser.getFriendList().remove(friendUser);
            friendUser.getFriendList().remove(myUser);

            friendUser.getViewedTasks().forEach(task -> {
                if (task.getUser().getId().equals(myId)) {
                    tasks.add(task);
                }
            });

            tasks.forEach(friendUser.getViewedTasks()::remove);
            this.userRepository.save(friendUser);
            this.userRepository.save(myUser);
            changeRequestStatus(myUser, friendUser);
        } else {
            throw new EntityNotFoundException(myId, User.class);
        }
    }

    private void changeRequestStatus(User sender, User receiver) {
        Optional<FriendRequest> optionalRequest = this.friendRequestRepository.findBySender_IdAndReceiver_Id(sender.getId(), receiver.getId());
        Optional<FriendRequest> optionalRequestIvn = this.friendRequestRepository.findBySender_IdAndReceiver_Id(receiver.getId(), sender.getId());

        if (optionalRequest.isPresent()) {
            FriendRequest friendRequest = optionalRequest.get();
            this.friendRequestRepository.delete(friendRequest);
        }

        if (optionalRequestIvn.isPresent()) {
            FriendRequest friendRequest = optionalRequestIvn.get();
            this.friendRequestRepository.delete(friendRequest);
        }
    }

    public ResponseEntity<?> editUser(EditUserDTO userUpdate) {
        User dbUser = this.one(userUpdate.getId());
        if (dbUser != null) {
            if (!dbUser.getUsername().equalsIgnoreCase(userUpdate.getUsername())) {
                if (this.userRepository.existsByUsername(userUpdate.getUsername())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Username already in use!"));
                }
            }

            if (!dbUser.getEmail().equalsIgnoreCase(userUpdate.getEmail())) {
                if (this.userRepository.existsByEmail(userUpdate.getEmail())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Email already in use!"));
                }
            }

            boolean matchPass = encoder.matches(userUpdate.getPassword(), dbUser.getPassword());

            if (matchPass) {
                dbUser.setUsername(userUpdate.getUsername());
                dbUser.setEmail(userUpdate.getEmail());

                if (!StringUtils.isEmpty(userUpdate.getNewPass())) {
                    dbUser.setPassword(encoder.encode(userUpdate.getNewPass()));
                }

                userRepository.save(dbUser);

                log.info("Usuario actualizado en la base de datos: {}", dbUser.getUsername());

                return ResponseEntity.ok(userUpdate);
            }

            return ResponseEntity.badRequest().body(new MessageResponse("Error: Passwords do not match!"));
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Error: User does not exist!"));
    }

    public ResponseEntity<?> editState(User userUpdate) {
        User dbUser = this.one(userUpdate.getId());
        if (dbUser != null) {

            dbUser.setState(userUpdate.getState());
            userRepository.save(dbUser);

            log.info("Se ha actulizado el status del usuario: {}", dbUser.getState());

            return ResponseEntity.ok(userUpdate);
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Error: User does not exist!"));
    }

    public ResponseEntity<?> editBio(User userUpdate) {
        User dbUser = this.one(userUpdate.getId());
        if (dbUser != null) {

            dbUser.setBiography(userUpdate.getBiography());
            userRepository.save(dbUser);

            log.info("Se ha actulizado la bio del usuario: {}", dbUser.getBiography());

            return ResponseEntity.ok(userUpdate);
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Error: User does not exist!"));
    }


}
