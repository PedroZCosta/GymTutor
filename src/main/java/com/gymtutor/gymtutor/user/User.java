package com.gymtutor.gymtutor.user;

import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanModel;
import com.gymtutor.gymtutor.commonusers.workoutplanperuser.WorkoutPlanPerUserModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @OneToMany(mappedBy = "user")
    private List<WorkoutPlanPerUserModel> workoutPlanPerUserModels = new ArrayList<>();

    // Email (obrigatório e precisa ser um email válido)
    @Column(name = "email", unique = true)
    @NotBlank
    @Email(message = "Email inválido.")
    private String userEmail;

    // Senha (obrigatória e com tamanho mínimo de 5 caracteres)
    @NotBlank
    @Size(min = 5, message = "A senha deve ter pelo menos 5 caracteres.")
    private String userPassword;

    // CPF
    @Column(name = "cpf", unique = true)
    private String userCpf;

    // Usuario está ativo?
    private boolean isActive = true;

    // Usuario está bloqueado?
    private boolean isLocked = false;


    //todo: alterar para ManyToOne quando for criar CRUD de usuario
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;



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

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public boolean isLocked() { return isLocked; }
    public void setLocked(boolean isLocked) { this.isLocked = isLocked; }


}