package com.gymtutor.gymtutor.commonusers.workoutperworkoutplan;

import com.gymtutor.gymtutor.commonusers.workout.WorkoutModel;
import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// Serviço para gerenciar a lógica de negócios relacionada a entidades workoutPerWorkoutPlan (workoutPerWorkoutPlanService),
// incluindo operações de criação, atualização, exclusão e recuperação de entidades,
// além de realizar validações de permissão de acesso para o usuário logado.
@Service
public class WorkoutPerWorkoutPlanService {

    // todo: revisar esse codigo quando possivel
    @Autowired
    private WorkoutPerWorkoutPlanRepository workoutPerWorkoutPlanRepository;

    // Buscar vínculos pelo workoutId
    public List<WorkoutPerWorkoutPlanModel> findByWorkoutWorkoutId(int workoutId) {
        return workoutPerWorkoutPlanRepository.findByWorkoutWorkoutId(workoutId);
    }

    // Buscar vínculos pelo workoutPlanId
    public List<WorkoutPerWorkoutPlanModel> findByWorkoutPlanWorkoutPlanId(int workoutPlanId) {
        return workoutPerWorkoutPlanRepository.findByWorkoutPlanWorkoutPlanId(workoutPlanId);
    }

    // Vincular treino a uma ficha de treino
    @Transactional
    public void linkWorkoutToWorkoutPlan(WorkoutModel workout, WorkoutPlanModel workoutPlan) {
        // Verifica se já existe o vínculo
        Optional<WorkoutPerWorkoutPlanModel> existingLink = workoutPerWorkoutPlanRepository
                .findByWorkoutIdAndWorkoutPlanId(workout.getWorkoutId(), workoutPlan.getWorkoutPlanId());

        if (existingLink.isPresent()) {
            throw new IllegalStateException("Este treino já está vinculado à ficha de treino.");
        }

        WorkoutPerWorkoutPlanModel link = new WorkoutPerWorkoutPlanModel();
        link.setWorkout(workout);
        link.setWorkoutPlan(workoutPlan);

        workoutPerWorkoutPlanRepository.save(link);
    }

    // Desvincular treino da ficha de treino
    @Transactional
    public void unlinkWorkoutFromWorkoutPlan(WorkoutModel workout, WorkoutPlanModel workoutPlan) {
        Optional<WorkoutPerWorkoutPlanModel> existingLink = workoutPerWorkoutPlanRepository
                .findByWorkoutIdAndWorkoutPlanId(workout.getWorkoutId(), workoutPlan.getWorkoutPlanId());

        if (existingLink.isEmpty()) {
            throw new IllegalStateException("Este treino não está vinculado à ficha de treino.");
        }

        workoutPerWorkoutPlanRepository.delete(existingLink.get());
    }
}
