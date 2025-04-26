package com.gymtutor.gymtutor.personal.clientperuser;

import com.gymtutor.gymtutor.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="tb_clientPerUser")
public class ClientPerUserModel {

    @EmbeddedId
    private ClientPerUserRelationKey id;

    @ManyToOne
    @MapsId("personalId")
    @JoinColumn(name="personal_Id")
    private User personal;

    @ManyToOne
    @MapsId("clientId")
    @JoinColumn(name="client_id")
    private User client;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters e Setters
    public ClientPerUserRelationKey getId() {
        return id;
    }
    public void setId(ClientPerUserRelationKey id) {
        this.id = id;
    }

    public User getPersonal() {
        return personal;
    }
    public void setPersonal(User personal) {
        this.personal = personal;
    }

    public User getClient() {
        return client;
    }
    public void setClient(User client) {
        this.client = client;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}
