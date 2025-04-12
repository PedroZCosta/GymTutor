package com.gymtutor.gymtutor.commonusers.workoutplanperuser;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class WorkoutPlanPerUserId {

    @Column(name="workoutPlan_id")
    private int workoutPlanId;

    @Column(name="user_id")
    private int userId;

    public WorkoutPlanPerUserId() {
    }

    public WorkoutPlanPerUserId(int workoutPlanId, int userId) {
        this.workoutPlanId = workoutPlanId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkoutPlanPerUserId)) return false;
        WorkoutPlanPerUserId that = (WorkoutPlanPerUserId) o;
        return workoutPlanId == that.workoutPlanId &&
                userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(workoutPlanId, userId);
    }

    // Getters e setters
    public int getWorkoutPlanId() {
        return workoutPlanId;
    }

    public void setWorkoutPlanId(int workoutPlanId) {
        this.workoutPlanId = workoutPlanId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
