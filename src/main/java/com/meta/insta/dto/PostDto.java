package com.meta.insta.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PostDto {

        public record CreatePostRequest(
                        @NotBlank(message = "Image URL is required") String imageUrl,

                        String caption,

                        @NotNull(message = "Author user ID is required") Long authorId) {
        }

        public record PostResponse(
                        Long id,
                        String imageUrl,
                        String caption,
                        UserDto.UserSummaryResponse author,
                        long likesCount,
                        long commentsCount,
                        boolean isLikedByCurrentUser,
                        Instant createdAt) {
        }
}
