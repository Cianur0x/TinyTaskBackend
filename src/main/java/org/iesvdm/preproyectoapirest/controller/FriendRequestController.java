package org.iesvdm.preproyectoapirest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.preproyectoapirest.domain.FriendRequest;
import org.iesvdm.preproyectoapirest.dto.RequestDTO;
import org.iesvdm.preproyectoapirest.service.FriendRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/v1/api/request")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    @GetMapping(value = {"", "/"}, params = {"!search", "!order", "!page", "!size"})
    public List<FriendRequest> all() {
        log.info("Accediendo a todos los temas");
        return this.friendRequestService.all();
    }

    @PostMapping({"", "/"})
    public FriendRequest newFRequest(@RequestBody RequestDTO friendRequest) {
        return this.friendRequestService.save(friendRequest);
    }

    @GetMapping("/{id}")
    public FriendRequest one(@PathVariable("id") Long id) {
        return this.friendRequestService.one(id);
    }

    @PutMapping("/{id}")
    public FriendRequest replaceFRequest(@PathVariable("id") Long id, @RequestBody FriendRequest friendRequest) {
        return this.friendRequestService.replace(id, friendRequest);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteFRequest(@PathVariable("id") Long id) {
        this.friendRequestService.delete(id);
    }
}
