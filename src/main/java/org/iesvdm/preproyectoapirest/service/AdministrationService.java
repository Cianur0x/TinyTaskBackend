package org.iesvdm.preproyectoapirest.service;

import org.iesvdm.preproyectoapirest.domain.ARole;
import org.iesvdm.preproyectoapirest.domain.Role;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.dto.ManageUserDTO;
import org.iesvdm.preproyectoapirest.dto.UserRoleDTO;
import org.iesvdm.preproyectoapirest.mapper.ManageUserMapper;
import org.iesvdm.preproyectoapirest.repository.RoleRepository;
import org.iesvdm.preproyectoapirest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdministrationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ManageUserMapper manageUserMapper;
    private final RoleRepository rolRepository;

    public AdministrationService(UserRepository userRepository, UserService userService, ManageUserMapper manageUserMapper, RoleRepository rolRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.manageUserMapper = manageUserMapper;
        this.rolRepository = rolRepository;
    }

    public void delete(Long id) {
        this.userService.delete(id);
    }

    public List<ManageUserDTO> getAllUsers() {
        List<User> users = this.userRepository.findAll();
        List<ManageUserDTO> userDTOs = new ArrayList<>();

        users.forEach(user -> {
            ManageUserDTO userDTO = manageUserMapper.userToManUserDTO(user);
            userDTOs.add(userDTO);
        });

        return userDTOs;
    }

    public ManageUserDTO editUser(UserRoleDTO userRoleDTO) {
        Set<Role> roles = new HashSet<>();
        Optional<User> optionalUser = this.userRepository.findById(userRoleDTO.getId());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (userRoleDTO.getRol().equals("ROL_ADMIN")) {
                Role adminRole = rolRepository.findByRoleName(ARole.ROL_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                roles.add(adminRole);
            } else {
                Role userRole = rolRepository.findByRoleName(ARole.ROL_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                roles.add(userRole);
            }

            user.setRoles(roles);
            this.userRepository.save(user);

            return manageUserMapper.userToManUserDTO(user);
        }

        return null;
    }
}
