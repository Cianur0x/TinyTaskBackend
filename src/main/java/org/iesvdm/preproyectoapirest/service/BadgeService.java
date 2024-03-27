package org.iesvdm.preproyectoapirest.service;

import org.iesvdm.preproyectoapirest.domain.Badge;
import org.iesvdm.preproyectoapirest.exception.EntityNotFoundException;
import org.iesvdm.preproyectoapirest.repository.BadgeRepository;
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
public class BadgeService {
    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;

    public BadgeService(BadgeRepository badgeRepository, UserRepository userRepository) {
        this.badgeRepository = badgeRepository;
        this.userRepository = userRepository;
    }

    public List<Badge> all() {
        return this.badgeRepository.findAll();
    }

    public List<Badge> all(Optional<String> findOpt, Optional<String> orderOpt) {
        Sort sort = null;

        if (orderOpt.isPresent()) {
            sort = orderOpt.get().equals("desc") ? Sort.by("badgeName").descending() : Sort.by("badgeName").ascending();
        }

        if (findOpt.isPresent()) {
            return this.badgeRepository.findBadgeByBadgeNameContainingIgnoreCase(findOpt.get(), sort);
        } else {
            return sort != null ? this.badgeRepository.findAll(sort) :
                    this.badgeRepository.findAll();
        }
    }

    public Map<String, Object> all(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("badgeName").ascending());
        Page<Badge> pageAll = this.badgeRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();

        response.put("badges", pageAll.getContent());
        response.put("currentPage", pageAll.getNumber());
        response.put("totalItems", pageAll.getTotalElements());
        response.put("totalPages", pageAll.getTotalPages());

        return response;
    }

    public Badge save(Badge user) {
        return this.badgeRepository.save(user);
    }

    public Badge one(Long id) {
        return this.badgeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Badge.class));
    }

    public Badge replace(Long id, Badge badge) {
        return this.badgeRepository.findById(id).map(p -> (id.equals(badge.getId()) ?
                        this.badgeRepository.save(badge) : null))
                .orElseThrow(() -> new EntityNotFoundException(id, Badge.class));
    }

    public void delete(Long id) {
        this.badgeRepository.findById(id).map(p -> {
            this.badgeRepository.delete(p);
            return p;
        }).orElseThrow(() -> new EntityNotFoundException(id, Badge.class));
    }
}
