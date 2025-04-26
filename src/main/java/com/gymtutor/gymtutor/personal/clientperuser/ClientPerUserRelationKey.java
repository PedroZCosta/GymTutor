package com.gymtutor.gymtutor.personal.clientperuser;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ClientPerUserRelationKey implements Serializable {

    private int personalId;
    private int clientId;

    public ClientPerUserRelationKey() {
    }

    public ClientPerUserRelationKey(int personalId, int clientId) {
        this.personalId = personalId;
        this.clientId = clientId;
    }

    public int getPersonalId() {
        return personalId;
    }
    public void setPersonalId(int personalId) {
        this.personalId = personalId;
    }

    public int getClientId() {
        return clientId;
    }
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientPerUserRelationKey)) return false;
        ClientPerUserRelationKey that = (ClientPerUserRelationKey) o;
        return personalId == that.personalId && clientId == that.clientId;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(personalId, clientId);
    }

}
