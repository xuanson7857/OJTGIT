package com.ojt.responsitoty;

import com.ojt.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmailIgnoreCase(String emailId);

    Optional<User> findByUsername(String username);
}
