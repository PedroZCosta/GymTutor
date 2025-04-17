package com.gymtutor.gymtutor.commonusers.workoutplanperuser;

// DTO individual para vínculo ficha <-> usuário
public class WorkoutPlanPerUserFormDTO {

    private int userId;
    private boolean isLinked;

    // Getters e setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isLinked() {
        return isLinked;
    }

    public void setLinked(boolean linked) {
        isLinked = linked;
    }
}