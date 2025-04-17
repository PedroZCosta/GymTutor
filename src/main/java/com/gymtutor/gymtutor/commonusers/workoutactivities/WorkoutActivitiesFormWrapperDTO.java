package com.gymtutor.gymtutor.commonusers.workoutactivities;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class WorkoutActivitiesFormWrapperDTO {
    @Valid
    @NotNull(message = "A lista de atividades não pode ser nula.")
    private List<WorkoutActivityFormDTO> workoutActivities;

    // Getters e setters
    public List<WorkoutActivityFormDTO> getWorkoutActivities() {
        return workoutActivities;
    }

    public void setWorkoutActivities(List<WorkoutActivityFormDTO> workoutActivities) {
        this.workoutActivities = workoutActivities;
    }
}
