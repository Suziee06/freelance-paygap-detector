package com.freelancedetector.repository;

import com.freelancedetector.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByGender(String gender);
    boolean existsByEmail(String email);
}
