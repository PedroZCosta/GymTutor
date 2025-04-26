package com.gymtutor.gymtutor.commonusers.workoutplanperuser;

import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanModel;
import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanService;
import com.gymtutor.gymtutor.user.User;
import com.gymtutor.gymtutor.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutPlanPerUserService {

    @Autowired
    private WorkoutPlanPerUserRepository workoutPlanPerUserRepository;

    @Autowired
    private WorkoutPlanService workoutPlanService;

    @Autowired
    private UserService userService;

    // Vincular ficha de treino a um usuario
    @Transactional
    public void linkWorkoutPlanToUser(WorkoutPlanModel workoutPlan, User user){

        WorkoutPlanPerUserId id = new WorkoutPlanPerUserId();
        id.setWorkoutPlanId(workoutPlan.getWorkoutPlanId());
        id.setUserId(user.getUserId());

        WorkoutPlanPerUserModel link = new WorkoutPlanPerUserModel();
        link.setWorkoutPlanPerUserId(id);
        link.setWorkoutPlan(workoutPlan);
        link.setUser(user);

        workoutPlanPerUserRepository.save(link);
    }

    // Desvincular treino da ficha de treino
    @Transactional
    public void unlinkWorkoutPlanPerUser(int workoutPlanId, int userId) {
        var id = new WorkoutPlanPerUserId();
        id.setWorkoutPlanId(workoutPlanId);
        id.setUserId(userId);

        Optional<WorkoutPlanPerUserModel> existingLink = workoutPlanPerUserRepository.findByWorkoutPlanPerUserId(id);

        existingLink.ifPresent(workoutPlanPerUserRepository::delete);
    }

    public WorkoutPlanPerUserModel findById(int WorkoutPlanPerUserId){
        Optional<WorkoutPlanPerUserModel> optionalWorkoutPlanPerUserModel = workoutPlanPerUserRepository.findById(WorkoutPlanPerUserId);
        return optionalWorkoutPlanPerUserModel.orElseThrow(() -> new RuntimeException("workoutPlanPerUser not found with id " + WorkoutPlanPerUserId));
    }

    public List<WorkoutPlanPerUserModel> findAllByWorkoutPlanId(int workoutPlanId) {
        return workoutPlanPerUserRepository.findByWorkoutPlanWorkoutPlanId(workoutPlanId);
    }

    @Transactional
    public void linkUserToPlan(int workoutPlanId, int userId) {
        WorkoutPlanModel workoutPlan = workoutPlanService.findById(workoutPlanId);
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        WorkoutPlanPerUserId id = new WorkoutPlanPerUserId(
                workoutPlanId, userId
        );
        WorkoutPlanPerUserModel link = new WorkoutPlanPerUserModel();
        link.setWorkoutPlanPerUserId(id);
        link.setWorkoutPlan(workoutPlan);
        link.setUser(user);
        workoutPlanPerUserRepository.save(link);


    }




}
