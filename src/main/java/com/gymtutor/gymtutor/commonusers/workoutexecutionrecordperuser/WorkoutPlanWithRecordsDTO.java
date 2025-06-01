package com.gymtutor.gymtutor.commonusers.workoutexecutionrecordperuser;

import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanModel;

import java.util.List;

public class WorkoutPlanWithRecordsDTO {

    private WorkoutPlanModel workoutPlan;

    private List<WorkoutExecutionRecordPerUserModel> records;

    public WorkoutPlanWithRecordsDTO() {
    }

    public WorkoutPlanModel getWorkoutPlan() {
        return workoutPlan;
    }

    public void setWorkoutPlan(WorkoutPlanModel workoutPlan) {
        this.workoutPlan = workoutPlan;
    }

    public List<WorkoutExecutionRecordPerUserModel> getRecords() {
        return records;
    }

    public void setRecords(List<WorkoutExecutionRecordPerUserModel> records) {
        this.records = records;
    }
}
