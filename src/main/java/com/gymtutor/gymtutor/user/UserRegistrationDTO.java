package com.gymtutor.gymtutor.user;

import com.gymtutor.gymtutor.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

@FieldMatch(first = "userEmail", second = "confirmEmail", message = "Os e-mails não coincidem.")
@FieldMatch(first = "userPassword", second = "confirmPassword", message = "As senhas não coincidem.")
public class UserRegistrationDTO {

    @NotBlank
    @Size(min = 3, message = "O nome deve ter pelo menos 3 caracteres.")
    private String userName;

    @NotBlank
    @Email(message = "Email inválido.")
    private String userEmail;

    @NotBlank
    @Email(message = "Confirmação de email inválida.")
    private String confirmEmail;

    @NotBlank
    @Size(min = 5, message = "A senha deve ter pelo menos 5 caracteres.")
    private String userPassword;

    @NotBlank
    private String confirmPassword;

    @CPF(message = "CPF inválido.")
    @NotBlank
    private String userCpf;

    // Getters e Setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getConfirmEmail() { return confirmEmail; }
    public void setConfirmEmail(String confirmEmail) { this.confirmEmail = confirmEmail; }

    public String getUserPassword() { return userPassword; }
    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getUserCpf() { return userCpf; }
    public void setUserCpf(String userCpf) { this.userCpf = userCpf; }
}
