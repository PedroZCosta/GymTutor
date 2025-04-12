package com.gymtutor.gymtutor.commonusers.workoutplan;


import com.gymtutor.gymtutor.security.CustomUserDetails;
import com.gymtutor.gymtutor.user.User;
import com.gymtutor.gymtutor.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Serviço para gerenciar a lógica de negócios relacionada a entidades WorkoutPlan (WorkoutPlanService),
// incluindo operações de criação, atualização, exclusão e recuperação de entidades,
// além de realizar validações de permissão de acesso para o usuário logado.
@Service
public class WorkoutPlanService {

    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    private UserRepository userRepository;


    public void createWorkoutPlan(WorkoutPlanModel workoutPlanModel, CustomUserDetails loggedUser) {
        User user = loggedUser.getUser();
        workoutPlanModel.setUser(user);
        workoutPlanRepository.save(workoutPlanModel);}

    public WorkoutPlanModel findById(int workoutPlanId){
        Optional<WorkoutPlanModel> optionalWorkoutPlanModel = workoutPlanRepository.findById(workoutPlanId);
        return optionalWorkoutPlanModel.orElseThrow(() -> new RuntimeException("workoutPlan not found with id " + workoutPlanId));
    }

    public List<WorkoutPlanModel> findAllByUserUserId(int userId) {
        return workoutPlanRepository.findAllByUserUserId(userId);
    }

    public void updateWorkoutPlan(WorkoutPlanModel workoutPlanModel, int workoutPlanId){

        // Busca a ficha de treino existente pelo ID
        Optional<WorkoutPlanModel> existingWorkoutPlanModel = workoutPlanRepository.findById(workoutPlanId);

        // Se a ficha de treino for encontrada, atualiza os dados
        if (existingWorkoutPlanModel.isPresent()){
            WorkoutPlanModel workoutPlan = existingWorkoutPlanModel.get();

            workoutPlan.setWorkoutPlanName(workoutPlanModel.getWorkoutPlanName());
            workoutPlan.setWorkoutPlanDescription(workoutPlanModel.getWorkoutPlanDescription());


            // Salva o treino atualizada no banco de dados
            workoutPlanRepository.save(workoutPlan);

        }
    }

    public void deleteWorkoutPlan(int workoutPlanId){
        workoutPlanRepository.deleteById(workoutPlanId);
    }

}