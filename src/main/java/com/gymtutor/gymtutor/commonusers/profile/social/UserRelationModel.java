package com.gymtutor.gymtutor.commonusers.profile.social;

import com.gymtutor.gymtutor.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_user_relation")
public class UserRelationModel {

    @EmbeddedId
    private UserRelationKey id;

    @ManyToOne
    @MapsId("senderId")
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @MapsId("receiverId")
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private boolean accepted = false;

    private LocalDateTime requestedAt = LocalDateTime.now();

    private LocalDateTime acceptedAt;

    // Getters e Setters
    public UserRelationKey getId() { return id; }
    public void setId(UserRelationKey id) { this.id = id; }

    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }

    public User getReceiver() { return receiver; }
    public void setReceiver(User receiver) { this.receiver = receiver; }

    public boolean isAccepted() { return accepted; }
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
        if (accepted) this.acceptedAt = LocalDateTime.now();
    }

    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }

    public LocalDateTime getAcceptedAt() { return acceptedAt; }
    public void setAcceptedAt(LocalDateTime acceptedAt) { this.acceptedAt = acceptedAt; }
}