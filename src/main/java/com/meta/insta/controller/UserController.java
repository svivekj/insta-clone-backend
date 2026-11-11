package com.meta.insta.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.meta.insta.dto.UserDto.RegisterRequest;
import com.meta.insta.dto.UserDto.UserProfileResponse;
import com.meta.insta.dto.UserDto.UserSummaryResponse;
// import com.meta.insta.model.User;
import com.meta.insta.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserProfileResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        UserProfileResponse registerUser = userService.registerUser(request);
        return new ResponseEntity<>(registerUser, HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable String username) {
        UserProfileResponse profile = userService.getUserProfile(username);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/id/{userId}/summary")
    public ResponseEntity<UserSummaryResponse> getUserSummary(@PathVariable Long userId) {
        UserSummaryResponse summary = userService.getUserSummary(userId);
        return ResponseEntity.ok(summary);
    }

    @PostMapping("/{targetUserId}/follow")
    public ResponseEntity<Void> followUser(@PathVariable Long targetUserId, @RequestParam Long currentUserId) {
        userService.followUser(currentUserId, targetUserId);
        return ResponseEntity.ok().build();
    }
}
