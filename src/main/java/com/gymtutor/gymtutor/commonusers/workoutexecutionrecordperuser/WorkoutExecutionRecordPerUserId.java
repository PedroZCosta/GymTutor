package com.gymtutor.gymtutor.commonusers.workoutexecutionrecordperuser;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WorkoutExecutionRecordPerUserId implements Serializable {

    @Column(name = "sender_id", nullable = true)
    private Integer senderId;

    @Column(name = "receiver_id", nullable = false)
    private Integer receiverId;

    @Column(name = "workoutPlan_id", nullable = false)
    private Integer workoutPlanId;

    @Column(name = "workout_id", nullable = false)
    private Integer workoutId;

    public WorkoutExecutionRecordPerUserId() {
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
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
        return Objects.equals(senderId, that.senderId) &&
                Objects.equals(receiverId, that.receiverId) &&
                Objects.equals(workoutPlanId, that.workoutPlanId) &&
                Objects.equals(workoutId, that.workoutId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderId, receiverId, workoutPlanId, workoutId);
    }
}