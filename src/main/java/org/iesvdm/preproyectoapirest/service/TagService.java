package org.iesvdm.preproyectoapirest.service;

import org.iesvdm.preproyectoapirest.domain.Tag;
import org.iesvdm.preproyectoapirest.domain.Task;
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

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> all() {
        return this.tagRepository.findAll();
    }

    public Map<String, Object> paginado(String[] paginado) {
        int pagina = Integer.parseInt(paginado[0]);
        int tamanio = Integer.parseInt(paginado[1]);

        Pageable paginacion = PageRequest.of(pagina, tamanio, Sort.by("id").ascending());
        Page<Tag> pageAll = this.tagRepository.findAll(paginacion);

        Map<String, Object> response = new HashMap<>();

        response.put("user", pageAll.getContent());
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
        return this.tagRepository.findById(id).map(p -> (id.equals(tag.getId()) ? this.tagRepository.save(tag) : null))
                .orElseThrow(() -> new EntityNotFoundException(id, Tag.class));
    }

    public void delete(Long id) {
        this.tagRepository.findById(id).map(p -> {
            this.tagRepository.delete(p);
            return p;
        }).orElseThrow(() -> new EntityNotFoundException(id, Tag.class));
    }
}
