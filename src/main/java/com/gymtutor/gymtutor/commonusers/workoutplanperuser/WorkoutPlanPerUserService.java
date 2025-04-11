package com.gymtutor.gymtutor.commonusers.workoutplanperuser;

import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanModel;
import com.gymtutor.gymtutor.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class WorkoutPlanPerUserService {

    @Autowired
    private WorkoutPlanPerUserRepository workoutPlanPerUserRepository;

    // Vincular ficha de treino a um usuario
    @Transactional
    public void linkWorkoutPlanToUser(WorkoutPlanModel workoutPlan, User user){
        // Verifica se já existe o vínculo
        Optional<WorkoutPlanPerUserModel> existingLink = workoutPlanPerUserRepository.findByUserUserIdAndWorkoutPlanWorkoutPlanId(workoutPlan.getWorkoutPlanId(), user.getUserId());

        if(existingLink.isPresent()){
            throw new IllegalStateException("Esta ficha de treino já está vinculada à este usuário.");
        }

        WorkoutPlanPerUserModel link = new WorkoutPlanPerUserModel();
        link.setWorkoutPlan(workoutPlan);
        link.setUser(user);

        workoutPlanPerUserRepository.save(link);
    }

    // Desvincular treino da ficha de treino
    @Transactional
    public void unlinkWorkoutPlanPerUser(WorkoutPlanModel workoutPlan, User user ){
        Optional<WorkoutPlanPerUserModel> existingLink = workoutPlanPerUserRepository.findByUserUserIdAndWorkoutPlanWorkoutPlanId(workoutPlan.getWorkoutPlanId(), user.getUserId());


        if (existingLink.isEmpty()){
            throw new IllegalStateException("Esta ficha de treino não está vinculada à este usuário.");
        }

        workoutPlanPerUserRepository.delete(existingLink.get());
    }



}
