package org.iesvdm.preproyectoapirest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.Theme;
import org.iesvdm.preproyectoapirest.service.ThemeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
// @CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/v1/api/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping(value = {"", "/"}, params = {"!search", "!order", "!page", "!size"})
    public List<Theme> all() {
        log.info("Accediendo a todos los temas");
        return this.themeService.all();
    }

    @GetMapping(value = {"", "/"}, params = {"!page", "!size"})
    public List<Theme> all(@RequestParam("search") Optional<String> findOpt,
                           @RequestParam("order") Optional<String> orderOpt) {
        log.info("Accediendo a todos los temas con filtros");
        return this.themeService.all(findOpt, orderOpt);
    }

    @GetMapping(value = {"", "/"})
    public ResponseEntity<Map<String, Object>> all(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "3") int size) {
        log.info("Accediendo a temas con paginación");
        Map<String, Object> responseAll = this.themeService.all(page, size);
        return ResponseEntity.ok(responseAll);
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
