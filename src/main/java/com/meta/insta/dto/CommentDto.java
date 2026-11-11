package com.meta.insta.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CommentDto {
    public record CreateCommentRequest(
            @NotBlank(message = "Comment text cannot be empty") String text,

            @NotNull(message = "User ID is required") Long userId) {
    }

    public record CommentResponse(
            Long id,
            String text,
            UserDto.UserSummaryResponse author,
            Instant createdAt) {
    }
}
