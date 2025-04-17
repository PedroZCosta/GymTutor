package com.gymtutor.gymtutor.commonusers.workoutactivities;

import com.gymtutor.gymtutor.activities.ActivitiesModel;
import com.gymtutor.gymtutor.commonusers.workout.WorkoutModel;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Serviço para gerenciar a lógica de negócios relacionada a entidades WorkoutActivities (WorkoutActivitiesService),
// incluindo operações de criação, atualização, exclusão e recuperação de entidades,
// além de realizar validações de permissão de acesso para o usuário logado.
@Service
public class WorkoutActivitiesService {

    @Autowired
    private WorkoutActivitiesRepository workoutActivitiesRepository;

    // Vincular atividade a um treino
    @Transactional
    public void linkWorkoutActivityToWorkout(WorkoutModel workout, ActivitiesModel activities, Byte sequence, String reps) {
        WorkoutActivitiesId id = new WorkoutActivitiesId();
        id.setWorkoutId(workout.getWorkoutId());
        id.setActivitiesId(activities.getActivitiesId());

        Optional<WorkoutActivitiesModel> existingLink = workoutActivitiesRepository.findByWorkoutActivitiesId(id);
        if (existingLink.isEmpty()) {
            WorkoutActivitiesModel link = new WorkoutActivitiesModel();
            link.setWorkoutActivitiesId(id);
            link.setWorkout(workout);
            link.setActivity(activities);
            link.setSequence(sequence);
            link.setReps(reps);

            workoutActivitiesRepository.save(link);
        }
    }

    // Desvincular atividade de um treino
    @Transactional
    public void unlinkWorkoutActivityFromWorkout(WorkoutModel workout, ActivitiesModel activities) {
        WorkoutActivitiesId id = new WorkoutActivitiesId();
        id.setWorkoutId(workout.getWorkoutId());
        id.setActivitiesId(activities.getActivitiesId());

        System.out.println("Tentando desvincular:");
        System.out.println("Workout ID: " + workout.getWorkoutId());
        System.out.println("Activity ID: " + activities.getActivitiesId());

        Optional<WorkoutActivitiesModel> existingLink = workoutActivitiesRepository.findByWorkoutActivitiesId(id);
        if (existingLink.isPresent()) {
            System.out.println("Encontrou o vínculo! Vai remover.");
            workoutActivitiesRepository.delete(existingLink.get());
        } else {
            System.out.println("NÃO encontrou o vínculo...");
        }
    }
}
