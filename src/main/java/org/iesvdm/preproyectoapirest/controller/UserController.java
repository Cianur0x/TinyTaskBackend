package org.iesvdm.preproyectoapirest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.MessageResponse;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.dto.UserDTO;
import org.iesvdm.preproyectoapirest.repository.UserRepository;
import org.iesvdm.preproyectoapirest.security.TokenUtils;
import org.iesvdm.preproyectoapirest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/v1/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenUtils tokenUtils;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"", "/"}, params = {"!search", "!order", "!page", "!size", "!friend", "!friendList", "!id"})
    public List<User> all() {
        log.info("Accediendo a todos los usuarios");
        return this.userService.all();
    }

    @GetMapping(value = {"", "/"}, params = {"!search", "!order", "!page", "!size", "!friendList",})
    public UserDTO addUserToFriend(@RequestParam("friend") Optional<String> findOpt,
                                   @RequestParam("id") Long id) {
        log.info("Añadiendo un friend requested{}id{}", findOpt.isPresent(), id);

        return this.userService.addUserToFriendList(findOpt, id);
    }

    @GetMapping(value = {"/friendlist"}, params = {"!search", "!order", "!page", "!size", "!friend",})
    public List<UserDTO> getFriendsList(@RequestParam("id") Long id) {
        log.info("Accediendo a la lista de amigos");

        return this.userService.getFriendList(id);
    }

    @GetMapping(value = {"", "/"}, params = {"!page", "!size", "!friend", "!id"})
    public List<User> all(@RequestParam("search") Optional<String> findOpt,
                          @RequestParam("order") Optional<String> orderOpt) {
        log.info("Accediendo a todOs las usuarios con filtros");
        return this.userService.all(findOpt, orderOpt);
    }

    @GetMapping(value = {"", "/"}, params = {"!friend", "!id"})
    public ResponseEntity<Map<String, Object>> all(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "3") int size) {
        log.info("Accediendo a usuarios con paginación");
        Map<String, Object> responseAll = this.userService.all(page, size);
        return ResponseEntity.ok(responseAll);
    }

    @PostMapping({"", "/"})
    public User newUser(@RequestBody User user) {
        return this.userService.save(user);
    }

    @GetMapping("/{id}")
    public User one(@PathVariable("id") Long id) {
        return this.userService.one(id);
    }

    @PutMapping("/{id}")
    public User replaceUser(@PathVariable("id") Long id, @RequestBody User user) {
        return this.userService.replace(id, user);
    }

    @PutMapping("/edituser")
    public ResponseEntity<?> editUser(@RequestBody User userUpdate) {
        User dbUser = this.userService.one(userUpdate.getId());
        if (dbUser != null) {
            if (!dbUser.getUsername().equalsIgnoreCase(userUpdate.getUsername())) {
                if (this.userRepository.existsByUsername(userUpdate.getUsername())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Username ya en uso!"));
                }
            }

            if (!dbUser.getEmail().equalsIgnoreCase(userUpdate.getEmail())) {
                if (this.userRepository.existsByEmail(userUpdate.getEmail())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Email ya en uso!"));
                }
            }

//            userUpdate.setPassword(encoder.encode(userUpdate.getPassword()));
//            userUpdate.setFriendList(dbUser.getFriendList());
//            userUpdate.setBadge(dbUser.getBadge());
//            userUpdate.setLastConnection(dbUser.getLastConnection());
//            userUpdate.setRoles(dbUser.getRoles());
//            userUpdate.setTags(dbUser.getTags());
//            userUpdate.setTareasCreadas(dbUser.getTareasCreadas());
//            userUpdate.setTheme(dbUser.getTheme());
//            userUpdate.setViewedTasks(dbUser.getViewedTasks());

            dbUser.setUsername(userUpdate.getUsername());
            dbUser.setEmail(userUpdate.getEmail());
            dbUser.setPassword(encoder.encode(userUpdate.getPassword()));

//             this.userService.replace(dbUser.getId(), userUpdate);
//            userRepository.save(userUpdate);

            userRepository.save(dbUser);

            log.info("Usuario actualizado en la base de datos{}", userUpdate.getUsername());


            return ResponseEntity.ok(userUpdate);
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Error: El usuario no existe!"));
    }

    // TODO dto user solo con id y state
    @PutMapping("/editstate")
    public ResponseEntity<?> editState(@RequestBody User userUpdate) {
        User dbUser = this.userService.one(userUpdate.getId());
        if (dbUser != null) {

            dbUser.setState(userUpdate.getState());
            userRepository.save(dbUser);

            log.info("Se ha actulizado el status del usuario: {}", dbUser.getState());

            return ResponseEntity.ok(userUpdate);
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Error: El usuario no existe!"));
    }

    // TODO dto user solo con id y bio
    @PutMapping("/editbio")
    public ResponseEntity<?> editBio(@RequestBody User userUpdate) {
        User dbUser = this.userService.one(userUpdate.getId());
        if (dbUser != null) {

            dbUser.setBiography(userUpdate.getBiography());
            userRepository.save(dbUser);

            log.info("Se ha actulizado la bio del usuario: {}", dbUser.getBiography());

            return ResponseEntity.ok(userUpdate);
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Error: El usuario no existe!"));
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        this.userService.delete(id);
    }
}
