package com.gymtutor.gymtutor.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tb_personal")
public class Personal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personalId;

    @NotBlank
    private String personalCREEF;


    //todo: repensar na relacao entre essas tabelas
    @OneToOne
    private User user;

    // Getters and Setters
    public int getPersonalId() {
        return personalId;
    }

    public void setPersonalId(int personalId) {
        this.personalId = personalId;
    }

    public String getPersonalCREEF() {
        return personalCREEF;
    }

    public void setPersonalCREEF(String personalCREEF) {
        this.personalCREEF = personalCREEF;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}