package com.gymtutor.gymtutor.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalRepository extends JpaRepository<Personal, Integer> {
    // Você pode adicionar métodos personalizados para buscar um Personal se necessário
}