package org.iesvdm.preproyectoapirest.controller;

import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import org.iesvdm.preproyectoapirest.domain.*;
import org.iesvdm.preproyectoapirest.repository.RoleRepository;
import org.iesvdm.preproyectoapirest.repository.UserRepository;
import org.iesvdm.preproyectoapirest.security.TokenUtils;
import org.iesvdm.preproyectoapirest.service.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/v1/api/auth")
@NoArgsConstructor
public class AuthController {
    AuthenticationManager authenticationManager;

    UserRepository userRepository;

    RoleRepository rolRepository;

    PasswordEncoder encoder;


    TokenUtils tokenUtils;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository rolRepository, PasswordEncoder encoder, TokenUtils tokenUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.encoder = encoder;
        this.tokenUtils = tokenUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticateUser(@Valid @RequestBody UserLoginDTO loginRequest) {

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

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrerDTO registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username ya en uso!"));
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email ya en uso!"));
        }

        // Create new user's account
        User user = new User(registerRequest.getUsername(),
                registerRequest.getEmail(),
                encoder.encode(registerRequest.getPassword()));

        Set<String> strRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = rolRepository.findByRoleNameAdmin(ARole.ROL_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = rolRepository.findByRoleNameAdmin(ARole.ROL_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                        roles.add(adminRole);

                        break;

                    default:
                        Role userRole = rolRepository.findByRoleNameAdmin(ARole.ROL_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Usuario registrado correctamente!"));
    }

}
