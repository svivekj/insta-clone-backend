package com.meta.insta.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.Repository;

import com.meta.insta.model.Post;

// @Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    Page<Post> findByAuthorIdInOrderByCreatedAtDesc(List<Long> authorIds, Pageable pageable);

    long countByAuthorId(Long authorId);

    @Query("SELECT p FROM Post p JOIN FETCH p.author WHERE p.id = :postId")
    Post findPostWithAuthor(@Param("postId") Long postId);
}
