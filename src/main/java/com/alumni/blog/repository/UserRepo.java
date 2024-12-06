package com.alumni.blog.repository;

import com.alumni.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u JOIN u.roles r WHERE r.name = 'ROLE_ADMIN'")
    boolean existsByRoles_Name(String roleName);

}
