package com.gymtutor.gymtutor.commonusers.workoutplanperuser;


import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanModel;
import com.gymtutor.gymtutor.user.User;
import jakarta.persistence.*;

@Entity
@Table(name="tb_workoutPerUser")
public class WorkoutPlanPerUserModel {

    @EmbeddedId
    private WorkoutPlanPerUserId workoutPlanPerUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workoutPlan_id", insertable = false, updatable = false)
    private WorkoutPlanModel workoutPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    public WorkoutPlanPerUserModel() { // Construtor padrão necessário para JPA
    }

    public WorkoutPlanPerUserModel(WorkoutPlanPerUserId workoutPlanPerUserId, WorkoutPlanModel workoutPlan, User user) {
        this.workoutPlanPerUserId = workoutPlanPerUserId;
        this.workoutPlan = workoutPlan;
        this.user = user;
    }

    //Getters e setters
    public WorkoutPlanPerUserId getWorkoutPlanPerUserId() {
        return workoutPlanPerUserId;
    }

    public void setWorkoutPlanPerUserId(WorkoutPlanPerUserId workoutPlanPerUserId) {
        this.workoutPlanPerUserId = workoutPlanPerUserId;
    }

    public WorkoutPlanModel getWorkoutPlan() {
        return workoutPlan;
    }

    public void setWorkoutPlan(WorkoutPlanModel workoutPlan) {
        this.workoutPlan = workoutPlan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



}
