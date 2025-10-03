package com.ses.salessupport.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ses.salessupport.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySupabaseUserId(String supabaseUserId);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByRole(User.UserRole role);

    List<User> findByIsActive(Boolean isActive);

    List<User> findByRoleAndIsActive(User.UserRole role, Boolean isActive);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword%")
    List<User> findByUsernameOrEmailContaining(@Param("keyword") String keyword);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    Long countByRole(@Param("role") User.UserRole role);

    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    Long countActiveUsers();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsBySupabaseUserId(String supabaseUserId);
}
