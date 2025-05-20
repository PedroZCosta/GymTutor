package com.gymtutor.gymtutor.commonusers.webchat;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gymtutor.gymtutor.user.User;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByUser1AndUser2(User user1, User user2);
    Optional<Conversation> findByUser2AndUser1(User user2, User user1);

    default Optional<Conversation> findBetweenUsers(User user1, User user2) {
        return findByUser1AndUser2(user1, user2).or(() -> findByUser2AndUser1(user1, user2));
    }

}