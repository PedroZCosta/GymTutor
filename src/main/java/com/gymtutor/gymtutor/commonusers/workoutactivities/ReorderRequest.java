package com.gymtutor.gymtutor.commonusers.workoutactivities;

public class ReorderRequest {

    private int activitiesId;

    private byte sequence;

    public int getActivitiesId() {
        return activitiesId;
    }

    public void setActivitiesId(int activitiesId) {
        this.activitiesId = activitiesId;
    }

    public byte getSequence() {
        return sequence;
    }

    public void setSequence(byte sequence) {
        this.sequence = sequence;
    }
}