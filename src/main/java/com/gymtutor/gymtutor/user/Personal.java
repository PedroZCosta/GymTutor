package com.gymtutor.gymtutor.user;

import jakarta.persistence.*;

@Entity
public class Personal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String creef;

    @OneToOne
    private User user;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreef() {
        return creef;
    }

    public void setCreef(String creef) {
        this.creef = creef;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}