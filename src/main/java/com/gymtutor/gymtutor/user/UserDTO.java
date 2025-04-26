package com.gymtutor.gymtutor.user;

import jakarta.validation.constraints.NotNull;

public class UserDTO {

    @NotNull
    private Integer userId;

    @NotNull
    private Integer workoutPlanId;

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
