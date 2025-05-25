package com.gymtutor.gymtutor.commonusers.webchat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageModel, Long> {
}