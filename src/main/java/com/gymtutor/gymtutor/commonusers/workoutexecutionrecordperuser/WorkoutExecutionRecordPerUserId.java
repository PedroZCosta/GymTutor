package com.gymtutor.gymtutor.commonusers.workoutexecutionrecordperuser;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WorkoutExecutionRecordPerUserId implements Serializable {

    @Column(name="user_id", nullable = false)
    private Integer userId;

    @Column(name="workoutPlan_id", nullable = false)
    private Integer workoutPlanId;

    @Column(name="workout_id", nullable = false)
    private Integer workoutId;

    public WorkoutExecutionRecordPerUserId() {
    }

    public WorkoutExecutionRecordPerUserId(Integer userId, Integer workoutPlanId, Integer workoutId) {
        this.userId = userId;
        this.workoutPlanId = workoutPlanId;
        this.workoutId = workoutId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getWorkoutPlanId() {
        return workoutPlanId;
    }

    public void setWorkoutPlanId(Integer workoutPlanId) {
        this.workoutPlanId = workoutPlanId;
    }

    public Integer getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Integer workoutId) {
        this.workoutId = workoutId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkoutExecutionRecordPerUserId)) return false;
        WorkoutExecutionRecordPerUserId that = (WorkoutExecutionRecordPerUserId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(workoutPlanId, that.workoutPlanId) &&
                Objects.equals(workoutId, that.workoutId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, workoutPlanId, workoutId);
    }
}