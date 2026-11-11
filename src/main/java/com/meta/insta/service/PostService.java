package com.meta.insta.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meta.insta.dto.CommentDto.CommentResponse;
import com.meta.insta.dto.CommentDto.CreateCommentRequest;
import com.meta.insta.dto.PostDto.CreatePostRequest;
import com.meta.insta.dto.PostDto.PostResponse;
import com.meta.insta.dto.UserDto.UserSummaryResponse;
import com.meta.insta.model.Comment;
import com.meta.insta.model.Like;
import com.meta.insta.model.Post;
import com.meta.insta.model.User;
import com.meta.insta.repository.CommentRepository;
import com.meta.insta.repository.LikeRepository;
import com.meta.insta.repository.PostRepository;
import com.meta.insta.repository.UserRepository;

@Service
public class PostService {
        private final PostRepository postRepository;
        private final UserRepository userRepository;
        private final LikeRepository likeRepository;
        private final CommentRepository commentRepository;

        public PostService(
                        PostRepository postRepository,
                        UserRepository userRepository,
                        LikeRepository likeRepository,
                        CommentRepository commentRepository) {
                this.postRepository = postRepository;
                this.userRepository = userRepository;
                this.likeRepository = likeRepository;
                this.commentRepository = commentRepository;
        };

        @Transactional
        public PostResponse createPost(CreatePostRequest request) {
                User author = userRepository.findById(request.authorId())
                                .orElseThrow(() -> new RuntimeException("Author user not found"));

                Post post = Post.builder()
                                .imageUrl(request.imageUrl())
                                .caption(request.caption())
                                .author(author)
                                .build();

                Post savedPost = postRepository.save(post);
                return mapToPostResponse(savedPost, author.getId());
        }

        @Transactional(readOnly = true)
        public PostResponse getPostById(Long postId, Long currentUserId) {
                Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new RuntimeException("Post not found"));
                return mapToPostResponse(post, currentUserId);
        }

        // Paginated Home Feed: Posts from users that currentUserId follows + self
        @Transactional(readOnly = true)
        public Page<PostResponse> getHomeFeed(Long currentUserId, Pageable pageable) {
                List<Long> followedIds = new ArrayList<>(userRepository.findFollowingIdsByUserId(currentUserId));
                followedIds.add(currentUserId);

                Page<Post> postsPage = postRepository.findByAuthorIdInOrderByCreatedAtDesc(followedIds, pageable);
                return postsPage.map(post -> mapToPostResponse(post, currentUserId));
        }

        // Profile Feed: All posts created by a specific user
        @Transactional(readOnly = true)
        public List<PostResponse> getUserPosts(Long authorId, Long currentUserId) {
                List<Post> posts = postRepository.findByAuthorIdOrderByCreatedAtDesc(authorId);
                return posts.stream()
                                .map(post -> mapToPostResponse(post, currentUserId))
                                .toList();
        }

        @Transactional
        public boolean toogleLike(Long postId, Long userId) {
                Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new RuntimeException("Post not found"));
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                boolean exists = likeRepository.existsByPostIdAndUserId(postId, userId);
                if (exists) {
                        likeRepository.deleteByPostIdAndUserId(postId, userId);
                        return false;
                } else {
                        Like like = Like.builder()
                                        .post(post)
                                        .user(user)
                                        .build();
                        likeRepository.save(like);
                        return true;
                }
        }

        @Transactional
        public CommentResponse addComment(Long postId, CreateCommentRequest request) {
                Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new RuntimeException("Post not found"));
                User user = userRepository.findById(request.userId())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Comment comment = Comment.builder()
                                .text(request.text())
                                .post(post)
                                .user(user)
                                .build();

                Comment savedComment = commentRepository.save(comment);

                UserSummaryResponse authorSummary = new UserSummaryResponse(user.getId(), user.getUsername(),
                                user.getProfilePictureUrl());

                return new CommentResponse(
                                savedComment.getId(),
                                savedComment.getText(),
                                authorSummary,
                                savedComment.getCreatedAt());
        }

        @Transactional(readOnly = true)
        public List<CommentResponse> getPostComments(Long postId) {
                return commentRepository.findByPostIdWithUser(postId).stream()
                                .map(comment -> new CommentResponse(
                                                comment.getId(),
                                                comment.getText(),
                                                new UserSummaryResponse(
                                                                comment.getUser().getId(),
                                                                comment.getUser().getUsername(),
                                                                comment.getUser().getProfilePictureUrl()),
                                                comment.getCreatedAt()))
                                .toList();
        }

        private PostResponse mapToPostResponse(Post post, Long currentUserId) {
                long likesCount = likeRepository.countByPostId(post.getId());
                long commentsCount = commentRepository.countByPostId(post.getId());

                boolean isLikedByCurrentUser = currentUserId != null
                                && likeRepository.existsByPostIdAndUserId(post.getId(), currentUserId);

                UserSummaryResponse authorSummary = new UserSummaryResponse(
                                post.getAuthor().getId(),
                                post.getAuthor().getUsername(),
                                post.getAuthor().getProfilePictureUrl());

                return new PostResponse(
                                post.getId(),
                                post.getImageUrl(),
                                post.getCaption(),
                                authorSummary,
                                likesCount,
                                commentsCount,
                                isLikedByCurrentUser,
                                post.getCreatedAt());
        }

}
