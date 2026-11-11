package com.meta.insta.service;

import org.springframework.stereotype.Service;

import com.meta.insta.dto.UserDto.RegisterRequest;
import com.meta.insta.dto.UserDto.UserProfileResponse;
import com.meta.insta.dto.UserDto.UserSummaryResponse;
import com.meta.insta.model.User;
import com.meta.insta.repository.PostRepository;
import com.meta.insta.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public UserProfileResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already taken");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .bio(request.bio())
                .build();

        User savedUser = userRepository.save(user);
        return mapToProfileResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(String username) {
        User user = userRepository.findByUsername(
                username).orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return mapToProfileResponse(user);
    }

    @Transactional
    public void followUser(Long currentUserId, Long targetUserId) {
        if (currentUserId.equals(targetUserId)) {
            throw new IllegalArgumentException("You cannot follow yourself");
        }

        User currentUser = userRepository.findByIdWithFollowers(currentUserId)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Tharget user to follow not found"));

        if (targetUser.getFollowers().contains(currentUser)) {
            targetUser.getFollowers().remove(currentUser);
        } else {
            targetUser.getFollowers().add(currentUser);
        }

        userRepository.save(targetUser);
    }

    public UserSummaryResponse getUserSummary(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserSummaryResponse(user.getId(), user.getUsername(), user.getProfilePictureUrl());
    }

    private UserProfileResponse mapToProfileResponse(User user) {
        long postsCount = postRepository.countByAuthorId(user.getId());
        long followersCount = user.getFollowers() != null ? user.getFollowers().size() : 0;
        long followingCount = user.getFollowing() != null ? user.getFollowers().size() : 0;

        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfilePictureUrl(),
                user.getBio(),
                postsCount,
                followersCount,
                followingCount);
    }
}
