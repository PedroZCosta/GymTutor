package com.gymtutor.gymtutor.commonusers.workout;

import com.gymtutor.gymtutor.commonusers.workoutactivities.WorkoutActivitiesModel;
import com.gymtutor.gymtutor.commonusers.workoutperworkoutplan.WorkoutPerWorkoutPlanModel;
import com.gymtutor.gymtutor.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "tb_workout")
public class WorkoutModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workoutId;


    @NotBlank(message = "Nome não pode estar vazio!")
    @Size(min = 2, max=200 , message = "Este campo deve ter entre 2 e 200 caracteres.")
    private String workoutName;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false) // FK para UserModel
    private User user;

    @OneToMany(mappedBy = "workout")
    private List<WorkoutActivitiesModel> workoutActivities; // Relacionamento com WorkoutActivities

    @NotBlank(message = "Descanso não pode estar vazio!")
    @Size(min = 10, max=30 , message = "Este campo deve ter entre 10 e 30 caracteres.")
    private String restTime;

    @OneToMany(mappedBy = "workout")
    private List<WorkoutPerWorkoutPlanModel> workoutPerWorkoutPlans;

    @NotNull
    private int receiverUserId;

    // Construtor padrão
    public WorkoutModel() {
    }

    // Construtor com argumentos
    public WorkoutModel(int workoutId, User user, List<WorkoutActivitiesModel> workoutActivities, String restTime) {
        this.workoutId = workoutId;
        this.user = user;
        this.workoutActivities = workoutActivities;
        this.restTime = restTime;
    }


    // Getters e setters


    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public List<WorkoutActivitiesModel> getWorkoutActivities() {
        return workoutActivities;
    }

    public void setWorkoutActivities(List<WorkoutActivitiesModel> workoutActivities) {
        this.workoutActivities = workoutActivities;
    }

    public String getRestTime() {
        return restTime;
    }

    public void setRestTime(String restTime) {
        this.restTime = restTime;
    }

    public List<WorkoutPerWorkoutPlanModel> getWorkoutPerWorkoutPlans() {
        return workoutPerWorkoutPlans;
    }

    public void setWorkoutPerWorkoutPlans(List<WorkoutPerWorkoutPlanModel> workoutPerWorkoutPlans) {
        this.workoutPerWorkoutPlans = workoutPerWorkoutPlans;
    }

    public int getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(int receiverUserId) {
        this.receiverUserId = receiverUserId;
    }
}
