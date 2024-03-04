package org.iesvdm.preproyectoapirest.service;

import org.iesvdm.preproyectoapirest.domain.Tag;
import org.iesvdm.preproyectoapirest.domain.Theme;
import org.iesvdm.preproyectoapirest.exception.EntityNotFoundException;
import org.iesvdm.preproyectoapirest.repository.TagRepository;
import org.iesvdm.preproyectoapirest.repository.ThemeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> all() {
        return this.themeRepository.findAll();
    }

    public Map<String, Object> paginado(String[] paginado) {
        int pagina = Integer.parseInt(paginado[0]);
        int tamanio = Integer.parseInt(paginado[1]);

        Pageable paginacion = PageRequest.of(pagina, tamanio, Sort.by("id").ascending());
        Page<Theme> pageAll = this.themeRepository.findAll(paginacion);

        Map<String, Object> response = new HashMap<>();

        response.put("user", pageAll.getContent());
        response.put("currentPage", pageAll.getNumber());
        response.put("totalItems", pageAll.getTotalElements());
        response.put("totalPages", pageAll.getTotalPages());

        return response;
    }

    public Theme save(Theme tag) {
        return this.themeRepository.save(tag);
    }

    public Theme one(Long id) {
        return this.themeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Theme.class));
    }

    public Theme replace(Long id, Theme theme) {
        return this.themeRepository.findById(id).map(p -> (id.equals(theme.getId()) ? this.themeRepository.save(theme) : null))
                .orElseThrow(() -> new EntityNotFoundException(id, Theme.class));
    }

    public void delete(Long id) {
        this.themeRepository.findById(id).map(p -> {
            this.themeRepository.delete(p);
            return p;
        }).orElseThrow(() -> new EntityNotFoundException(id, Theme.class));
    }
}
