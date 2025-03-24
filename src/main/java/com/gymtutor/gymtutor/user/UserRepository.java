package com.gymtutor.gymtutor.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // Consulta para encontrar um usuário pelo email
    Optional<User> findByEmail(String email);
}