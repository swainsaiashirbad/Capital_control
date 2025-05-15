package com.auth_service.repository;

import com.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
  Optional<User> findByEmail(String email);
    boolean existsByEmail(String username);
}
