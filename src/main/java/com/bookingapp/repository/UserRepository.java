package com.bookingapp.repository;

import com.bookingapp.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<User> findUserByEmail(String email);
    
    @EntityGraph(attributePaths = "roles")
    Optional<User> findUserById(Long id);

    boolean existsByEmail(String email);
}
