package com.gymtutor.gymtutor.commonusers.workoutexecutionrecordperuser;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_completed_workoutplan")
public class CompletedWorkoutPlanModel {

    @EmbeddedId
    private CompletedWorkoutPlanId completedWorkoutPlanId;

    private boolean completed;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public CompletedWorkoutPlanModel() {}


    // getters e setters
    public CompletedWorkoutPlanId getCompletedWorkoutPlanId() {
        return completedWorkoutPlanId;
    }

    public void setCompletedWorkoutPlanId(CompletedWorkoutPlanId completedWorkoutPlanId) {
        this.completedWorkoutPlanId = completedWorkoutPlanId;
    }

    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}