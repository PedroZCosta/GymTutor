package com.gymtutor.gymtutor.commonusers.workoutexecutionrecordperuser;


import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

// Classe auxiliar para chave composta
@Embeddable
public class CompletedWorkoutPlanId implements Serializable {
    private Integer userId;
    private Integer workoutPlanId;

    public CompletedWorkoutPlanId() {}

    public CompletedWorkoutPlanId(Integer userId, Integer workoutPlanId) {
        this.userId = userId;
        this.workoutPlanId = workoutPlanId;
    }

    // equals e hashCode baseados nos dois campos
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompletedWorkoutPlanId)) return false;
        CompletedWorkoutPlanId that = (CompletedWorkoutPlanId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(workoutPlanId, that.workoutPlanId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, workoutPlanId);
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
}
