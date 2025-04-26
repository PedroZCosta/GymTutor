package com.gymtutor.gymtutor.personal.clientperuser;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ClientPerUserRepository extends JpaRepository<ClientPerUserModel, ClientPerUserRelationKey> {

    // Buscar todos os vínculos de um personal específico
    List<ClientPerUserModel> findByPersonalUserId(int personalId);

    // Buscar todos os vínculos de um cliente específico
    List<ClientPerUserModel> findByClientUserId(int clientId);

    // Buscar por ID composto
    Optional<ClientPerUserModel> findById(ClientPerUserRelationKey id);

    // Buscar vínculo específico de personal para cliente
    Optional<ClientPerUserModel> findByPersonalUserIdAndClientUserId(int personalId, int clientId);

}