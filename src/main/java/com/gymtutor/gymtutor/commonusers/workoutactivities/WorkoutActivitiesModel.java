package com.gymtutor.gymtutor.commonusers.workoutactivities;


import com.gymtutor.gymtutor.admin.activities.ActivitiesModel;
import com.gymtutor.gymtutor.commonusers.workout.WorkoutModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_workout_activities")
public class WorkoutActivitiesModel {
    @EmbeddedId
    private WorkoutActivitiesId workoutActivitiesId;

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false, updatable = false, insertable = false) // FK para Workout
    private WorkoutModel workout;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false, updatable = false, insertable = false) // FK para Activity
    private ActivitiesModel activity;

    @NotBlank(message = "Repetição não pode estar vazio!")
    @Size(min = 1, max=20 , message = "Este campo deve ter entre 1 e 20 caracteres.")
    private String reps; // Número de repetições

    @NotNull(message = "Sequência não pode estar vazia!")
    private Byte sequence; // Número de sequências



    public WorkoutActivitiesModel() {}

    public WorkoutActivitiesModel(WorkoutModel workout, ActivitiesModel activity, String reps, byte sequence) {
        this.workout = workout;
        this.activity = activity;
        this.reps = reps;
        this.sequence = sequence;
    }

    public WorkoutActivitiesId getWorkoutActivitiesId() {
        return workoutActivitiesId;
    }

    public void setWorkoutActivitiesId(WorkoutActivitiesId workoutActivitiesId) {
        this.workoutActivitiesId = workoutActivitiesId;
    }

    public String getReps() { return reps; }
    public void setReps(String reps) { this.reps = reps; }

    public Byte getSequence() { return sequence; }
    public void setSequence(Byte sequence) { this.sequence = sequence; }

    public WorkoutModel getWorkout() { return workout; }
    public void setWorkout(WorkoutModel workout) { this.workout = workout; }

    public ActivitiesModel getActivity() { return activity; }
    public void setActivity(ActivitiesModel activity) { this.activity = activity; }
}

