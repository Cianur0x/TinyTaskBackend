package org.iesvdm.preproyectoapirest.service;

import org.iesvdm.preproyectoapirest.domain.Role;
import org.iesvdm.preproyectoapirest.exception.EntityNotFoundException;
import org.iesvdm.preproyectoapirest.repository.RoleRepository;
import org.iesvdm.preproyectoapirest.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public List<Role> all() {
        return this.roleRepository.findAll();
    }

    public List<Role> all(Optional<String> findOpt, Optional<String> orderOpt) {
        Sort sort = null;

        if (orderOpt.isPresent()) {
            sort = orderOpt.get().equals("desc") ? Sort.by("roleName").descending() : Sort.by("roleName").ascending();
        }

        if (findOpt.isPresent()) {
            return this.roleRepository.findRoleByRoleNameContainingIgnoreCase(findOpt.get(), sort);
        } else {
            return sort != null ? this.roleRepository.findAll(sort) :
                    this.roleRepository.findAll();
        }
    }

    public Map<String, Object> all(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("roleName").ascending());
        Page<Role> pageAll = this.roleRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();

        response.put("roles", pageAll.getContent());
        response.put("currentPage", pageAll.getNumber());
        response.put("totalItems", pageAll.getTotalElements());
        response.put("totalPages", pageAll.getTotalPages());

        return response;
    }

    public Role save(Role role) {
        return this.roleRepository.save(role);
    }

    public Role one(Long id) {
        return this.roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Role.class));
    }

    public Role replace(Long id, Role role) {
        return this.roleRepository.findById(id).map(p -> (id.equals(role.getId()) ? this.roleRepository.save(role) : null))
                .orElseThrow(() -> new EntityNotFoundException(id, Role.class));
    }

    public void delete(Long id) {
        this.roleRepository.findById(id).map(p -> {
            p.getUserSet().forEach(user -> {
                user.setRoles(null);
                this.userRepository.save(user);
            });
            this.roleRepository.delete(p);
            return p;
        }).orElseThrow(() -> new EntityNotFoundException(id, Role.class));
    }
}
