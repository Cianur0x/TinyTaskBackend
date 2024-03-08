package org.iesvdm.preproyectoapirest.service;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.exception.EntityNotFoundException;
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

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        // TODO
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
