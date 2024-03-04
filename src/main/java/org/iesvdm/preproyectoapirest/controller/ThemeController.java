package org.iesvdm.preproyectoapirest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.Theme;
import org.iesvdm.preproyectoapirest.service.ThemeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
// @CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping(value = {"", "/"})
    public List<Theme> all() {
        log.info("Accediendo a todos los temas");
        return this.themeService.all();
    }

    @PostMapping({"", "/"})
    public Theme newTheme(@RequestBody Theme theme) {
        return this.themeService.save(theme);
    }

    @GetMapping("/{id}")
    public Theme one(@PathVariable("id") Long id) {
        return this.themeService.one(id);
    }

    @PutMapping("/{id}")
    public Theme replaceTheme(@PathVariable("id") Long id, @RequestBody Theme theme) {
        return this.themeService.replace(id, theme);

    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTheme(@PathVariable("id") Long id) {
        this.themeService.delete(id);
    }
}
