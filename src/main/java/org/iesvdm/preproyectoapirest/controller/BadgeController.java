package org.iesvdm.preproyectoapirest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.Badge;
import org.iesvdm.preproyectoapirest.service.BadgeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "https://tiny-task-v1.vercel.app")
@RequestMapping("/v1/api/badges")
public class BadgeController {

    private final BadgeService badgeService;

    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @GetMapping(value = {"", "/"}, params = {"!search", "!order", "!page", "!size"})
    public List<Badge> all() {
        log.info("Accediendo a todos los símbolos");
        return this.badgeService.all();
    }

    @GetMapping(value = {"", "/"}, params = {"!page", "!size"})
    public List<Badge> all(@RequestParam("search") Optional<String> findOpt,
                           @RequestParam("order") Optional<String> orderOpt) {
        log.info("Accediendo a todas los símbolos con filtros");
        return this.badgeService.all(findOpt, orderOpt);
    }

    @GetMapping(value = {"", "/"})
    public ResponseEntity<Map<String, Object>> all(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "3") int size) {
        log.info("Accediendo a símbolos con paginación");
        Map<String, Object> responseAll = this.badgeService.all(page, size);
        return ResponseEntity.ok(responseAll);
    }

    @PostMapping({"", "/"})
    public Badge newBadge(@RequestBody Badge badge) {
        return this.badgeService.save(badge);
    }

    @GetMapping("/{id}")
    public Badge one(@PathVariable("id") Long id) {
        return this.badgeService.one(id);
    }

    @GetMapping("/userbadge/{id}")
    public Badge getUserBadge(@PathVariable("id") Long id) {
        return this.badgeService.getUserBadge(id);
    }

    @PutMapping("/{id}")
    public Badge replaceBadge(@PathVariable("id") Long id, @RequestBody Badge badge) {
        return this.badgeService.replace(id, badge);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBadge(@PathVariable("id") Long id) {
        this.badgeService.delete(id);
    }

}
