package com.gymtutor.gymtutor.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "tb_user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email"}),
                @UniqueConstraint(columnNames = {"cpf"})
        }
)
public class User {

    // Identificador único do usuário (gerado automaticamente no banco)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    // Nome do aluno (obrigatório e tamanho mínimo de 2 caracteres)
    @Size(min = 2, message = "O nome deve ter pelo menos 2 caracteres.")
    private String userName;

    // Email (obrigatório e precisa ser um email válido)
    @NotBlank
    @Email(message = "Email inválido.")
    private String userEmail;

    // Senha (obrigatória e com tamanho mínimo de 5 caracteres)
    @NotBlank
    @Size(min = 5, message = "A senha deve ter pelo menos 5 caracteres.")
    private String userPassword;

    // CPF
    private String userCpf;

    //todo: alterar para ManyToOne quando for criar CRUD de usuario
    @ManyToMany(fetch = FetchType.EAGER)// Cria uma tabela relacionar entre o papel e o usuario
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserCpf() {
        return userCpf;
    }

    public void setUserCpf(String userCpf) {
        this.userCpf = userCpf;
    }

    public Set<Role> getRoles() { return roles; }

    public void setRoles(Set<Role> roles) { this.roles = roles; }
}