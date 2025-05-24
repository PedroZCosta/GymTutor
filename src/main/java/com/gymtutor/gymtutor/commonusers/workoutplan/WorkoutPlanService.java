package com.gymtutor.gymtutor.commonusers.workoutplan;


import com.gymtutor.gymtutor.commonusers.workoutplanperuser.WorkoutPlanPerUserId;
import com.gymtutor.gymtutor.commonusers.workoutplanperuser.WorkoutPlanPerUserModel;
import com.gymtutor.gymtutor.commonusers.workoutplanperuser.WorkoutPlanPerUserRepository;
import com.gymtutor.gymtutor.security.CustomUserDetails;
import com.gymtutor.gymtutor.user.User;
import com.gymtutor.gymtutor.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Serviço para gerenciar a lógica de negócios relacionada a entidades WorkoutPlan (WorkoutPlanService),
// incluindo operações de criação, atualização, exclusão e recuperação de entidades,
// além de realizar validações de permissão de acesso para o usuário logado.
@Service
public class WorkoutPlanService {

    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkoutPlanPerUserRepository workoutPlanPerUserRepository;


    public void createWorkoutPlan(WorkoutPlanModel workoutPlanModel, CustomUserDetails loggedUser) {
        User user = loggedUser.getUser();
        workoutPlanModel.setUser(user);
        workoutPlanRepository.save(workoutPlanModel);}

    public WorkoutPlanModel findById(int workoutPlanId){
        Optional<WorkoutPlanModel> optionalWorkoutPlanModel = workoutPlanRepository.findById(workoutPlanId);
        return optionalWorkoutPlanModel.orElseThrow(() -> new RuntimeException("workoutPlan not found with id " + workoutPlanId));
    }

    // Retorna as fichas que foram associadas à aquele usuario
    public List<WorkoutPlanModel> findAllByCopiedForUserUserId(int userId) {
        return workoutPlanRepository.findAllByCopiedForUserUserId(userId);
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

    @Transactional
    public void linkUserToPlan(WorkoutPlanModel workoutPlan, User user) {
        WorkoutPlanPerUserId id = new WorkoutPlanPerUserId();
        id.setWorkoutPlanId(workoutPlan.getWorkoutPlanId());
        id.setUserId(user.getUserId());

        Optional<WorkoutPlanPerUserModel> existingLink = workoutPlanPerUserRepository.findByWorkoutPlanPerUserId(id);
        if (existingLink.isEmpty()) {
            WorkoutPlanPerUserModel link = new WorkoutPlanPerUserModel();
            link.setWorkoutPlanPerUserId(id);
            link.setWorkoutPlan(workoutPlan);
            link.setUser(user);

            workoutPlanPerUserRepository.save(link);
        }
    }

    public void saveClone(WorkoutPlanModel workoutPlanModel) {
        workoutPlanRepository.save(workoutPlanModel);
    }

    public List<User> findUsersLinkedToWorkoutPlan(int workoutPlanId) {
        return workoutPlanPerUserRepository
                .findByWorkoutPlanWorkoutPlanId(workoutPlanId)
                .stream()
                .map(WorkoutPlanPerUserModel::getUser)
                .collect(Collectors.toList());
    }

    @Transactional
    public void unlinkUserFromPlan(WorkoutPlanModel workoutPlan, User user) {
        WorkoutPlanPerUserId id = new WorkoutPlanPerUserId();
        id.setWorkoutPlanId(workoutPlan.getWorkoutPlanId());
        id.setUserId(user.getUserId());

        Optional<WorkoutPlanPerUserModel> existingLink = workoutPlanPerUserRepository.findByWorkoutPlanPerUserId(id);
        if (existingLink.isPresent()) {
            workoutPlanPerUserRepository.delete(existingLink.get());
        } else {
            System.out.println("Vínculo não encontrado.");
        }
    }

}