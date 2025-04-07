package com.gymtutor.gymtutor.commonusers.workout;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Serviço para gerenciar a lógica de negócios relacionada a entidades workout (WorkoutService),
// incluindo operações de criação, atualização, exclusão e recuperação de entidades,
// além de realizar validações de permissão de acesso para o usuário logado.
@Service
public class WorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;

    public void createWorkout(WorkoutModel workoutModel) {workoutRepository.save(workoutModel);}

    public WorkoutModel findById(int workoutId){
        Optional<WorkoutModel> optionalWorkoutModel = workoutRepository.findById(workoutId);
        return optionalWorkoutModel.orElseThrow(() -> new RuntimeException("workout not found with id" + workoutId));
    }

    public List<WorkoutModel> findAll() {return workoutRepository.findAll();}

    public void updateWorkout(WorkoutModel workoutModel, int workoutId){

        // Busca a atividade existente pelo ID
        Optional<WorkoutModel> existingWorkout = workoutRepository.findById(workoutId);

        // Se a atividade for encontrada, atualiza os dados
        if (existingWorkout.isPresent()){
            WorkoutModel workout = existingWorkout.get();

            // Atualiza os campos da atividade
            // todo: confirmar se e isso mesmo
            workoutModel.setWorkoutActivities(workoutModel.getWorkoutActivities());
            workoutModel.setWorkoutName(workoutModel.getWorkoutName());
            workoutModel.setRestTime(workoutModel.getRestTime());
            workoutModel.setUser(workoutModel.getUser());
            workoutModel.setWorkoutActivities(workoutModel.getWorkoutActivities());

            // Salva o treino atualizada no banco de dados
            workoutRepository.save(workout);

        }
    }

    public void deleteWorkout(int workoutId){
        workoutRepository.deleteById(workoutId);
    }

}
