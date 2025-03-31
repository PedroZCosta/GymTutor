package com.gymtutor.gymtutor.commonusers.workout;

import com.gymtutor.gymtutor.commonusers.workoutactivities.WorkoutActivitiesModel;
import com.gymtutor.gymtutor.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    private String name;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false) // FK para UserModel
    private User user;

    @OneToMany(mappedBy = "workout")
    private List<WorkoutActivitiesModel> workoutActivities; // Relacionamento com WorkoutActivities

    @NotBlank(message = "Repetição não pode estar vazio!")
    @Size(min = 1, max=20 , message = "Este campo deve ter entre 1 e 20 caracteres.")
    private String restTime;

    public WorkoutModel() {
    }

    public WorkoutModel(int workoutId, User user, List<WorkoutActivitiesModel> workoutActivities, String restTime) {
        this.workoutId = workoutId;
        this.user = user;
        this.workoutActivities = workoutActivities;
        this.restTime = restTime;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
