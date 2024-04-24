package org.iesvdm.preproyectoapirest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.Tag;
import org.iesvdm.preproyectoapirest.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/v1/api/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = {"", "/"}, params = {"!search", "!order", "!page", "!size"})
    public List<Tag> all() {
        log.info("Accediendo a todos las etiquetas");
        return this.tagService.all();
    }

    @GetMapping(value = {"", "/"}, params = {"!page", "!size"})
    public List<Tag> all(@RequestParam("search") Optional<String> findOpt,
                         @RequestParam("order") Optional<String> orderOpt) {
        log.info("Accediendo a todas las etiquetas con filtros");
        return this.tagService.all(findOpt, orderOpt);
    }

    @GetMapping(value = {"", "/"})
    public ResponseEntity<Map<String, Object>> all(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "3") int size) {
        log.info("Accediendo a las etiquetas con paginación");
        Map<String, Object> responseAll = this.tagService.all(page, size);
        return ResponseEntity.ok(responseAll);
    }

    @PostMapping({"", "/"})
    public Tag newTag(@RequestBody Tag tag) {
        return this.tagService.save(tag);
    }

    @GetMapping("/{id}")
    public Tag one(@PathVariable("id") Long id) {
        return this.tagService.one(id);
    }

    @PutMapping("/{id}")
    public Tag replaceTag(@PathVariable("id") Long id, @RequestBody Tag tag) {
        return this.tagService.replace(id, tag);

    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable("id") Long id) {
        this.tagService.delete(id);
    }
}
