package com.gymtutor.gymtutor.commonusers.profile.social;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserRelationKey implements Serializable {

    private int senderId;
    private int receiverId;

    public UserRelationKey() {}

    public UserRelationKey(int senderId, int receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRelationKey)) return false;
        UserRelationKey that = (UserRelationKey) o;
        return senderId == that.senderId && receiverId == that.receiverId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderId, receiverId);
    }
}