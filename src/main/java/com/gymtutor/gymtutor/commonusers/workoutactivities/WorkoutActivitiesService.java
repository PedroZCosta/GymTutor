package com.gymtutor.gymtutor.commonusers.workoutactivities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Serviço para gerenciar a lógica de negócios relacionada a entidades WorkoutActivities (WorkoutActivitiesService),
// incluindo operações de criação, atualização, exclusão e recuperação de entidades,
// além de realizar validações de permissão de acesso para o usuário logado.
@Service
public class WorkoutActivitiesService {

    @Autowired
    private WorkoutActivitiesRepository workoutActivitiesRepository;

    public void createWorkoutActivities(WorkoutActivitiesModel workoutActivitiesModel){
        workoutActivitiesRepository.save(workoutActivitiesModel);
    }

    public WorkoutActivitiesModel findById(int workoutActivitiesId){
        Optional<WorkoutActivitiesModel> optionalWorkoutActivitiesModel = workoutActivitiesRepository.findById(workoutActivitiesId);
        return optionalWorkoutActivitiesModel.orElseThrow(() -> new RuntimeException("workoutActivity not found with id " + workoutActivitiesId));
    }

    public List<WorkoutActivitiesModel> findAll() {return workoutActivitiesRepository.findAll();}

    public void updateWorkoutActivities(WorkoutActivitiesModel workoutActivitiesModel, int workoutActivitiesId){
        // Busca a o exercios do treino existente pelo ID
        Optional<WorkoutActivitiesModel> existingWorkoutActivities = workoutActivitiesRepository.findById(workoutActivitiesId);


        // Se o exercios do treino for encontrado, atualiza os dados
        if (existingWorkoutActivities.isPresent()){
            WorkoutActivitiesModel workout = existingWorkoutActivities.get();

            // todo: verificar se esta correto
            // Atualiza os campos do  exercios do treino
            workout.setActivity(workoutActivitiesModel.getActivity());
            workout.setWorkout(workoutActivitiesModel.getWorkout());
            workout.setReps(workoutActivitiesModel.getReps());
            workout.setSequence(workoutActivitiesModel.getSequence());

            // Salva o  exercios do treino atualizado no banco de dados
            workoutActivitiesRepository.save(workout);
        }
    }

    public void deleteWorkoutActivities(int workoutId){
        workoutActivitiesRepository.deleteById(workoutId);
    }
}
