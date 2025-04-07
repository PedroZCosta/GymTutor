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

    // Ao excluir um treino da ficha de treino deverá ser tirada a linha que a relaciona com a ficha e o treino especifico
    // todo: realizar crud da relacao ficha e treino!!!!

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workoutPlanId;

    @NotBlank(message = "Nome não pode estar vazio!")
    @Size(min = 2, max=200 , message = "Este campo deve ter entre 2 e 200 caracteres.")
    private String workoutPlanName;

    @NotBlank(message = "Nome não pode estar vazio!")
    @Size(min = 2, max=200 , message = "Este campo deve ter entre 2 e 200 caracteres.")
    private String workoutPlanDescription;

    @OneToOne
    @JoinColumn(name= "user_id")
    private User user;

    // Construtor padrão
    public WorkoutPlanModel() {
    }

    // Construtor com argumentos
    public WorkoutPlanModel(User user, String workoutPlanName, String workoutPlanDescription, List<WorkoutModel> workoutModel) {
        this.user = user;
        this.workoutPlanName = workoutPlanName;
        this.workoutPlanDescription = workoutPlanDescription;
    }

    // Getters e setters
    public String getworkoutPlanDescription() {
        return workoutPlanDescription;
    }

    public void setWorkoutPlanDescription(String workoutPlanDescription) {
        this.workoutPlanDescription = workoutPlanDescription;
    }

    public int getWorkoutPlanId() {
        return workoutPlanId;
    }

    public String getWorkoutPlanName() {
        return workoutPlanName;
    }

    public void setWorkoutPlanName(String workoutPlanName) {
        this.workoutPlanName = workoutPlanName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
