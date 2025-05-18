package com.gymtutor.gymtutor.user;

import com.gymtutor.gymtutor.validation.FieldMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@FieldMatch(first = "userPassword", second = "confirmPassword4", message = "As senhas não coincidem.")
public class UserChangePasswordDTO {

    @NotBlank
    @Size(min = 5, message = "A senha deve ter pelo menos 5 caracteres.")
    private String userPassword;

    @NotBlank
    private String confirmPassword4;

    // Getters e Setters
    public String getUserPassword() { return userPassword; }
    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }

    public String getConfirmPassword4() { return confirmPassword4; }
    public void setConfirmPassword4(String confirmPassword4) { this.confirmPassword4 = confirmPassword4; }
}
