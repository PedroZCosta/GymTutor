package com.gymtutor.gymtutor.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Consulta para encontrar um usuário pelo email
    Optional<User> findByUserEmail(String userEmail);
}