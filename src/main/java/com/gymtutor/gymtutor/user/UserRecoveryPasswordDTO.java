package com.gymtutor.gymtutor.user;

import com.gymtutor.gymtutor.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@FieldMatch(first = "userEmail", second = "confirmEmail", message = "Os e-mails não coincidem.")
public class UserRecoveryPasswordDTO {

    @NotBlank
    @Email(message = "Email inválido.")
    private String userEmail;

    @NotBlank
    @Email(message = "Confirmação de email inválida.")
    private String confirmEmail;

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getConfirmEmail() { return confirmEmail; }
    public void setConfirmEmail(String confirmEmail) { this.confirmEmail = confirmEmail; }

}
