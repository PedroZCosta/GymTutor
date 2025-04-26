package com.gymtutor.gymtutor.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangeNameDTO {

    @Size(min = 2, message = "O nome deve ter pelo menos 2 caracteres.")
    @NotBlank(message = "O nome não pode estar vazio.")
    private String newName;

    @NotBlank(message = "A senha é obrigatória.")
    private String confirmPassword3;

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getConfirmPassword3() {
        return confirmPassword3;
    }

    public void setConfirmPassword3(String confirmPassword3) {
        this.confirmPassword3 = confirmPassword3;
    }
}