package com.gymtutor.gymtutor.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Método para buscar a Role pelo nome
    Role findByNome(String nome);
}