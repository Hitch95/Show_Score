package com.example.show_score.repository;

import com.example.show_score.model.UserBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserBean, Long> {
    // Additional query methods can be defined here if needed
    Optional<UserBean> findByUsername(String username);
    Optional<UserBean> findByEmail(String email);
}
