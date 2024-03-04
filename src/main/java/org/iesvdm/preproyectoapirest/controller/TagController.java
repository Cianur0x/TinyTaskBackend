package org.iesvdm.preproyectoapirest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.Tag;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.service.TagService;
import org.iesvdm.preproyectoapirest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
// @CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = {"", "/"})
    public List<Tag> all() {
        log.info("Accediendo a todos los etiquetas");
        return this.tagService.all();
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
