package org.iesvdm.preproyectoapirest.controller;

import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.ARole;
import org.iesvdm.preproyectoapirest.domain.MessageResponse;
import org.iesvdm.preproyectoapirest.domain.Role;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.dto.UserLoginDTO;
import org.iesvdm.preproyectoapirest.dto.UserRegisterDTO;
import org.iesvdm.preproyectoapirest.repository.RoleRepository;
import org.iesvdm.preproyectoapirest.repository.UserRepository;
import org.iesvdm.preproyectoapirest.security.TokenUtils;
import org.iesvdm.preproyectoapirest.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(origins = "https://tiny-task-v1.vercel.app")
@RequestMapping("/v1/api/auth")
@NoArgsConstructor
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository rolRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    TokenUtils tokenUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserLoginDTO loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = tokenUtils.generateToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();

            response.put("token", token);
            response.put("id", userDetails.getId());
            response.put("username", userDetails.getUsername());
            response.put("email", userDetails.getEmail());
            response.put("roles", roles);

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Bad Credentials!"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDTO registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username already in use!"));
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email already in use!"));
        }

        // Create new user's account
        User user = new User(registerRequest.getUsername(),
                registerRequest.getEmail(),
                encoder.encode(registerRequest.getPassword()));

        Set<String> strRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = rolRepository.findByRoleName(ARole.ROL_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = rolRepository.findByRoleName(ARole.ROL_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = rolRepository.findByRoleName(ARole.ROL_MOD)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                        roles.add(modRole);
                        break;

                    default:
                        Role userRole = rolRepository.findByRoleName(ARole.ROL_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User successfully registered!"));
    }

}
