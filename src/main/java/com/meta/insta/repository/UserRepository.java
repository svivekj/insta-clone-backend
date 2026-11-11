package com.meta.insta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.Repository;

import com.meta.insta.model.User;

// @Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.followers WHERE u.id = :id")
    Optional<User> findByIdWithFollowers(@Param("id") Long id);

    @Query("SELECT f.id FROM User u JOIN u.following f WHERE u.id = :userId")
    List<Long> findFollowingIdsByUserId(@Param("userId") Long userId);
}
