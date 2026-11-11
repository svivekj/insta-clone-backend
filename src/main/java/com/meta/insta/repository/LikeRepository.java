package com.meta.insta.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meta.insta.model.Like;

// import org.springframework.stereotype.Repository;

// @Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByPostIdAndUserId(Long postId, Long userId);

    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);

    void deleteByPostIdAndUserId(Long postId, Long userId);

    long countByPostId(Long postId);
}
