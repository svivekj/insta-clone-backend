package com.meta.insta.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDto {
        public record RegisterRequest(
                        @NotBlank(message = "Username is required") @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters") String username,

                        @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,

                        @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be atleast 6 characters") String password,
                        String bio) {
        }

        public record loginRequest(
                        @NotBlank(message = "Username or Email is required") String UsernameOrEmail,
                        @NotBlank(message = "Password is required") String password) {
        }

        public record UserSummaryResponse(
                        Long id,
                        String username,
                        String profilePictureUrl) {
        }

        public record UserProfileResponse(
                        Long id,
                        String username,
                        String email,
                        String profilePictureUrl,
                        String bio,
                        long postsCount,
                        long followersCount,
                        long followingCount) {
        }
}
