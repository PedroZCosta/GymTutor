package com.gymtutor.gymtutor.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "tb_personal")
public class Personal {

    @Id
    private int personalId;

    @NotBlank
    private String personalCREF;

    @Enumerated(EnumType.STRING)
    @NotNull
    private State state;

    @OneToOne
    @MapsId
    @JoinColumn(name = "personalId") // mapeia o mesmo id do User
    private User user;

    private boolean isApproved = false;

    private boolean isRejected = false;

    @Size(min = 5, max = 50, message = "O motivo da recusa deve ter entre 5 e 50 caracteres")
    private String rejectReason;

    // Getters and Setters
    public int getPersonalId() {
        return personalId;
    }

    public void setPersonalId(int personalId) {
        this.personalId = personalId;
    }

    public String getPersonalCREEF() {
        return personalCREF;
    }

    public void setPersonalCREEF(String personalCREEF) {
        this.personalCREF = personalCREEF;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPersonalCREF() { return personalCREF; }

    public void setPersonalCREF(String personalCREF) { this.personalCREF = personalCREF; }

    public State getState() { return state; }

    public void setState(State state) { this.state = state; }

    public boolean isApproved() {return isApproved;}

    public void setApproved(boolean approved) {isApproved = approved;}

    public boolean isRejected() {return isRejected;}

    public void setRejected(boolean rejected) {isRejected = rejected;}

    public String getRejectReason() {return rejectReason;}

    public void setRejectReason(String rejectReason) {this.rejectReason = rejectReason;}
}

