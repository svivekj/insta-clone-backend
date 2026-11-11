package com.meta.insta.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.meta.insta.model.Comment;
import com.meta.insta.model.Like;
import com.meta.insta.model.Post;
import com.meta.insta.model.User;
import com.meta.insta.repository.CommentRepository;
import com.meta.insta.repository.LikeRepository;
import com.meta.insta.repository.PostRepository;
import com.meta.insta.repository.UserRepository;

import jakarta.transaction.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

        private final UserRepository userRepository;
        private final PostRepository postRepository;
        private final LikeRepository likeRepository;
        private final CommentRepository commentRepository;

        public DataInitializer(
                        UserRepository userRepository,
                        PostRepository postRepository,
                        LikeRepository likeRepository,
                        CommentRepository commentRepository) {
                this.userRepository = userRepository;
                this.postRepository = postRepository;
                this.likeRepository = likeRepository;
                this.commentRepository = commentRepository;
        }

        @Override
        @Transactional
        public void run(String... args) throws Exception {
                if (userRepository.count() > 0) {
                        System.out.println(" Database already seeded. Skipping initialization.");
                        return;
                }

                System.out.println("Seeding initial mock data inti PostgreSQL...");

                User alex = User.builder()
                                .username("alex_dev")
                                .email("alex@meta.com")
                                .password("yourname")
                                .profilePictureUrl("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150")
                                .bio("Software Engineer | Building Spring Boot & React Apps")
                                .build();

                User sarah = User.builder()
                                .username("sarah_design")
                                .email("sarah@meta.com")
                                .password("password123")
                                .profilePictureUrl("https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150")
                                .bio("UI/UX Designer • Visual Thinker")
                                .build();

                userRepository.saveAll(List.of(alex, sarah));

                Post post1 = Post.builder()
                                .imageUrl("https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=800")
                                .caption("Working on a new Spring Boot 3 + React project!")
                                .author(alex)
                                .build();

                postRepository.save(post1);

                Like like = Like.builder().post(post1).user(sarah).build();
                likeRepository.save(like);

                Comment comment = Comment.builder().post(post1).user(sarah).text("Looks great!").build();

                commentRepository.save(comment);

                System.out.println("!!! MOCK DATA INITIALIZED SUCCESSFULLY !!!");
        }

}
