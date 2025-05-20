package com.gymtutor.gymtutor.commonusers.webchat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageModel, Long> {
    // Aqui você pode adicionar consultas customizadas, se necessário.
}