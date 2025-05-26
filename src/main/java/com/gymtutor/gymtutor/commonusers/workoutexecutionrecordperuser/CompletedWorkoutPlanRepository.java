package com.gymtutor.gymtutor.commonusers.workoutexecutionrecordperuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedWorkoutPlanRepository extends JpaRepository<CompletedWorkoutPlanModel, CompletedWorkoutPlanId> {
}
