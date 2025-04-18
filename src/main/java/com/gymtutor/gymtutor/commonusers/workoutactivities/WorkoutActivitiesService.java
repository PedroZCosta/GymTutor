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
    public void linkWorkoutActivityToWorkout(WorkoutModel workout, ActivitiesModel activities, String reps) {
        WorkoutActivitiesId id = new WorkoutActivitiesId();
        id.setWorkoutId(workout.getWorkoutId());
        id.setActivitiesId(activities.getActivitiesId());

        Optional<WorkoutActivitiesModel> existingLink = workoutActivitiesRepository.findByWorkoutActivitiesId(id);
        if (existingLink.isEmpty()) {
            WorkoutActivitiesModel link = new WorkoutActivitiesModel();
            link.setWorkoutActivitiesId(id);
            link.setWorkout(workout);
            link.setActivity(activities);

            // Define a sequência automaticamente com base nas atividades já vinculadas
            int nextSequence = workoutActivitiesRepository.countByWorkout_WorkoutId(workout.getWorkoutId()) + 1;
            link.setSequence((byte) nextSequence);

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


        Optional<WorkoutActivitiesModel> existingLink = workoutActivitiesRepository.findByWorkoutActivitiesId(id);
        if (existingLink.isPresent()) {
            workoutActivitiesRepository.delete(existingLink.get());
        } else {
            System.out.println("NÃO encontrou o vínculo...");
        }
    }

    public void updateSequence(int workoutId, int activityId, byte sequence) {
        Optional<WorkoutActivitiesModel> relation = workoutActivitiesRepository
                .findByWorkoutActivitiesId_WorkoutIdAndWorkoutActivitiesId_ActivitiesId(workoutId, activityId);

        if (relation.isPresent()) {
            WorkoutActivitiesModel model = relation.get();
            model.setSequence(sequence);
            workoutActivitiesRepository.save(model); // isso atualiza
        }
    }
}
