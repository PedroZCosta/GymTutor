package com.gymtutor.gymtutor.commonusers.workoutexecutionrecordperuser;

import com.gymtutor.gymtutor.commonusers.workoutplanperuser.WorkoutPlanPerUserId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
@Entity
@Table(name = "tb_workout_execution_record_per_user")
public class WorkoutExecutionRecordPerUserModel {

    @EmbeddedId
    private WorkoutExecutionRecordPerUserId workoutExecutionRecordPerUserId;

    @Column(name = "execution_count", nullable = false)
    private short executionCount;

    @Column(name = "last_execution_time")
    private LocalDateTime lastExecutionTime;

    public WorkoutExecutionRecordPerUserModel() {
    }

    // getters e setters
    public WorkoutExecutionRecordPerUserId getWorkoutExecutionRecordPerUserId() {
        return workoutExecutionRecordPerUserId;
    }

    public void setWorkoutExecutionRecordPerUserId(WorkoutExecutionRecordPerUserId workoutExecutionRecordPerUserId) {
        this.workoutExecutionRecordPerUserId = workoutExecutionRecordPerUserId;
    }

    public short getExecutionCount() {
        return executionCount;
    }

    public void setExecutionCount(short executionCount) {
        this.executionCount = executionCount;
    }

    public LocalDateTime getLastExecutionTime() {
        return lastExecutionTime;
    }

    public void setLastExecutionTime(LocalDateTime lastExecutionTime) {
        this.lastExecutionTime = lastExecutionTime;
    }

}


