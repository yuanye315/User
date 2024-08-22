package com.example.backend.repository;

import com.example.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

public interface UserRepository extends JpaRepository<User, Long> {
    // 根据用户名查找用户
    // 使用 @Query 注解自定义查询

    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDateTime AND :endDateTime")
    Page<User> findByCreatedAtBetween(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            Pageable pageable
    );

    @Query("SELECT u FROM User u WHERE u.userName = :userName")
    Page<User> findByUserName(
            @Param("userName") String userName,
            Pageable pageable
    );

    @Query("SELECT u FROM User u")
    Page<User> findAll(
            Pageable pageable
    );

    @Query("SELECT u FROM User u WHERE u.userName = :userName AND u.createdAt BETWEEN :startDateTime AND :endDateTime")
    Page<User> findByUserNameAndCreatedAtBetween(
            @Param("userName") String userName,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            Pageable pageable
    );
}
