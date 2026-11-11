package com.meta.insta.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.meta.insta.dto.CommentDto.CommentResponse;
import com.meta.insta.dto.CommentDto.CreateCommentRequest;
import com.meta.insta.dto.PostDto.CreatePostRequest;
import com.meta.insta.dto.PostDto.PostResponse;
import com.meta.insta.service.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody CreatePostRequest request) {
        PostResponse createdPost = postService.createPost(request);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long postId,
            @RequestParam(required = false) Long currentUserId) {
        PostResponse post = postService.getPostById(postId, currentUserId);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/feed")
    public ResponseEntity<Page<PostResponse>> getHomeFeed(
            @RequestParam Long currentUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponse> feed = postService.getHomeFeed(currentUserId, pageable);
        return ResponseEntity.ok(feed);
    }

    @GetMapping("/user/{authorId}")
    public ResponseEntity<List<PostResponse>> getUserPosts(
            @PathVariable Long authorId,
            @RequestParam(required = false) Long currentUserId) {
        List<PostResponse> posts = postService.getUserPosts(authorId, currentUserId);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("{postId}/like")
    public ResponseEntity<Boolean> toggleLike(@PathVariable Long postId, @RequestParam Long userId) {
        boolean liked = postService.toogleLike(postId, userId);
        return ResponseEntity.ok(liked);
    }

    @PostMapping("{postId}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request) {
        CommentResponse comment = postService.addComment(postId, request);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getPostComments(@PathVariable Long postId) {
        List<CommentResponse> comments = postService.getPostComments(postId);
        return ResponseEntity.ok(comments);
    }
}
