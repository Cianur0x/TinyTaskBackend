package org.iesvdm.preproyectoapirest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.dto.EditUserDTO;
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
    public ResponseEntity<?> addUserToFriend(@RequestParam("friend") Optional<String> findOpt,
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

    @GetMapping("/getuser/{id}")
    public EditUserDTO getEditUser(@PathVariable("id") Long id) {
        return this.userService.getEditUser(id);
    }

    @PutMapping("/{id}")
    public User replaceUser(@PathVariable("id") Long id, @RequestBody User user) {
        return this.userService.replace(id, user);
    }

    @PutMapping("/edituser")
    public ResponseEntity<?> editUser(@RequestBody EditUserDTO userUpdate) {
        return this.userService.editUser(userUpdate);
    }

    // TODO dto user solo con id y state
    @PutMapping("/editstate")
    public ResponseEntity<?> editState(@RequestBody User userUpdate) {
        return this.userService.editState(userUpdate);
    }

    // TODO dto user solo con id y bio
    @PutMapping("/editbio")
    public ResponseEntity<?> editBio(@RequestBody User userUpdate) {
        return this.userService.editBio(userUpdate);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        this.userService.delete(id);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{myid}/{friendid}")
    public void deleteUser(@PathVariable("friendid") Long id, @PathVariable("myid") Long myId) {
        this.userService.deleteFriend(myId, id);
    }
}
