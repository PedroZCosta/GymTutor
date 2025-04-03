package com.gymtutor.gymtutor.commonusers.workoutplan;

import com.gymtutor.gymtutor.commonusers.workout.WorkoutModel;
import com.gymtutor.gymtutor.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name="tb_workoutPlan")
public class WorkoutPlanModel {

    // todo: revisar pois nao esta criando uma tabela associativa com 2 id e tambem o user id erta duplicando na tabela!
    // Ao excluir um treino da ficha de treino deverá ser tirada a linha que a relaciona com a ficha e o treino especifico
    // todo: realizar crud da relacao ficha e treino!!!!

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workoutPlanId;

    @NotBlank(message = "Nome não pode estar vazio!")
    @Size(min = 2, max=200 , message = "Este campo deve ter entre 2 e 200 caracteres.")
    private String nome;

    @NotBlank(message = "Nome não pode estar vazio!")
    @Size(min = 2, max=200 , message = "Este campo deve ter entre 2 e 200 caracteres.")
    private String descricao;

    @OneToMany(mappedBy = "workout")
    private List<WorkoutModel> workoutModel; // Relacionamento com WorkoutModel

    @OneToOne
    @JoinColumn(name= "user_id")
    private User user;

    // Construtor padrão
    public WorkoutPlanModel() {
    }

    // Construtor com argumentos
    public WorkoutPlanModel(User user, String nome, String descricao, List<WorkoutModel> workoutModel) {
        this.user = user;
        this.nome = nome;
        this.descricao = descricao;
        this.workoutModel = workoutModel;
    }

    // Getters e setters
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<WorkoutModel> getWorkoutModel() {
        return workoutModel;
    }

    public void setWorkoutModel(List<WorkoutModel> workoutModel) {
        this.workoutModel = workoutModel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
