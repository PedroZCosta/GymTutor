package com.gymtutor.gymtutor.commonusers.workoutperworkoutplan;

import jakarta.validation.constraints.NotNull;

public class WorkoutPerWorkoutPlanFormDTO {

    @NotNull(message = "O ID do treino não pode ser nulo.")
    private Integer workoutId;


    public Integer getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Integer workoutId) {
        this.workoutId = workoutId;
    }
}
