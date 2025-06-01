package com.gymtutor.gymtutor.personal.clientgraphs;

import com.gymtutor.gymtutor.commonusers.workoutexecutionrecordperuser.WorkoutExecutionRecordPerUserModel;
import com.gymtutor.gymtutor.commonusers.workoutexecutionrecordperuser.WorkoutPlanWithRecordsDTO;
import com.gymtutor.gymtutor.user.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.List;


public class ClientGraphDTO {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private List<WorkoutPlanWithRecordsDTO> workoutPlanWithRecordsList;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public ClientGraphDTO() {}

    public List<WorkoutPlanWithRecordsDTO> getWorkoutPlanWithRecordsList() {
        return workoutPlanWithRecordsList;
    }

    public void setWorkoutPlanWithRecordsList(List<WorkoutPlanWithRecordsDTO> workoutPlanWithRecordsList) {
        this.workoutPlanWithRecordsList = workoutPlanWithRecordsList;
    }
}
