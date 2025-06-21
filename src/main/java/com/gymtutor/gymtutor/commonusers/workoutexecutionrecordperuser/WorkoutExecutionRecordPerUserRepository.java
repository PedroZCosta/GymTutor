package com.gymtutor.gymtutor.commonusers.workoutexecutionrecordperuser;

import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanModel;
import com.gymtutor.gymtutor.commonusers.workoutplanperuser.WorkoutPlanPerUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutExecutionRecordPerUserRepository extends JpaRepository<WorkoutExecutionRecordPerUserModel, WorkoutExecutionRecordPerUserId> {

    // Tive que fazer isso pois usar o JPA para 3 parametros de id Embedded da uns B.O as vezes
    @Query("SELECT r FROM WorkoutExecutionRecordPerUserModel r " +
            "WHERE r.workoutExecutionRecordPerUserId.senderId = :senderId " +
            "AND r.workoutExecutionRecordPerUserId.receiverId = :receiverId " +
            "AND r.workoutExecutionRecordPerUserId.workoutPlanId = :workoutPlanId " +
            "AND r.workoutExecutionRecordPerUserId.workoutId = :workoutId")
    Optional<WorkoutExecutionRecordPerUserModel> findRecord(
            @Param("senderId") Integer senderId,
            @Param("receiverId") Integer receiverId,
            @Param("workoutPlanId") Integer workoutPlanId,
            @Param("workoutId") Integer workoutId
    );

    List<WorkoutExecutionRecordPerUserModel> findAllByWorkoutExecutionRecordPerUserId_ReceiverIdAndWorkoutExecutionRecordPerUserId_WorkoutPlanId(
            Integer receiverId, Integer workoutPlanId
    );

    @Query("SELECT w FROM WorkoutExecutionRecordPerUserModel w WHERE w.workoutExecutionRecordPerUserId.workoutPlanId = :workoutPlanId")
    List<WorkoutExecutionRecordPerUserModel> findAllByWorkoutPlanId(@Param("workoutPlanId") Integer workoutPlanId);

    List<WorkoutExecutionRecordPerUserModel> findAllByWorkoutExecutionRecordPerUserId_SenderId(Integer senderId);

    List<WorkoutExecutionRecordPerUserModel> findAllByWorkoutExecutionRecordPerUserId_SenderIdAndWorkoutExecutionRecordPerUserId_ReceiverId(
            Integer senderId, Integer receiverId);
}


