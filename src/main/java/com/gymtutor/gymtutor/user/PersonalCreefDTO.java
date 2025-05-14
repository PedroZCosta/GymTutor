package com.gymtutor.gymtutor.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


public class PersonalCreefDTO {

    @NotBlank(message = "O CREF é obrigatório.")
    @Pattern(
            regexp = "^[0-9]{1,6}-[GPC]$",
            message = "O CREF deve conter de 1 a 6 dígitos seguidos de um hífen e uma letra (G, P ou C). Exemplo: 123456-G"
    )
    private String personalCREEF;

    @NotNull(message = "O estado é obrigatório.")
    private State state;

    // Getters and Setters
    public String getPersonalCREEF() {
        return personalCREEF;
    }

    public void setPersonalCREEF(String personalCREEF) {
        this.personalCREEF = personalCREEF;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}