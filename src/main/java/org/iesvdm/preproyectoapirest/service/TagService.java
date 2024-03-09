package org.iesvdm.preproyectoapirest.service;

import org.iesvdm.preproyectoapirest.domain.Tag;
import org.iesvdm.preproyectoapirest.exception.EntityNotFoundException;
import org.iesvdm.preproyectoapirest.repository.TagRepository;
import org.iesvdm.preproyectoapirest.repository.TaskRepository;
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
public class TagService {
    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;

    public TagService(TagRepository tagRepository, TaskRepository taskRepository) {
        this.tagRepository = tagRepository;
        this.taskRepository = taskRepository;
    }

    public List<Tag> all() {
        return this.tagRepository.findAll();
    }

    public List<Tag> all(Optional<String> findOpt, Optional<String> orderOpt) {
        Sort sort = null;

        if (orderOpt.isPresent()) {
            sort = orderOpt.get().equals("desc") ? Sort.by("name").descending() : Sort.by("name").ascending();
        }

        if (findOpt.isPresent()) {
            return this.tagRepository.findTagsByNameContainingIgnoreCase(findOpt.get(), sort);
        } else {
            return sort != null ? this.tagRepository.findAll(sort) :
                    this.tagRepository.findAll();
        }
    }

    public Map<String, Object> all(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Tag> pageAll = this.tagRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();

        response.put("tags", pageAll.getContent());
        response.put("currentPage", pageAll.getNumber());
        response.put("totalItems", pageAll.getTotalElements());
        response.put("totalPages", pageAll.getTotalPages());

        return response;
    }

    public Tag save(Tag tag) {
        return this.tagRepository.save(tag);
    }

    public Tag one(Long id) {
        return this.tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Tag.class));
    }

    public Tag replace(Long id, Tag tag) {
        return this.tagRepository.findById(id)
                .map(p -> (id.equals(tag.getId()) ? this.tagRepository.save(tag) : null))
                .orElseThrow(() -> new EntityNotFoundException(id, Tag.class));
    }

    public void delete(Long id) {
        this.tagRepository.findById(id).map(p -> {
            p.getTasks().stream()
                    .filter(task -> task.getTag().getId().equals(p.getId()))
                    .forEach(task -> {
                        task.setTag(null);
                        this.taskRepository.save(task);
                    });
            this.tagRepository.delete(p);
            return p;
        }).orElseThrow(() -> new EntityNotFoundException(id, Tag.class));
    }
}
