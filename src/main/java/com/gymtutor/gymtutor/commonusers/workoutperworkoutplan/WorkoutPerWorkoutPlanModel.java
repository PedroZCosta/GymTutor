package com.gymtutor.gymtutor.commonusers.workoutperworkoutplan;

import com.gymtutor.gymtutor.commonusers.workout.WorkoutModel;
import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanModel;
import jakarta.persistence.*;

/**
 * Entidade JPA que representa a relação entre WorkoutModel e WorkoutPlanModel.
 * A tabela gerada será 'tb_workout_per_workoutplan'.
 */
@Entity
@Table(name = "tb_workout_per_workoutplan") // Nome da tabela que armazenará a relação entre treinos e fichas de treino
public class WorkoutPerWorkoutPlanModel {

    @EmbeddedId
    private WorkoutPerWorkoutPlanId workoutPerWorkoutPlanId; // Chave composta que representa a associação entre Workout e WorkoutPlan

    @ManyToOne(fetch = FetchType.LAZY) // Relacionamento muitos-para-um com WorkoutModel (um treino)
    @JoinColumn(name = "workout_id", insertable = false, updatable = false)
    private WorkoutModel workout; // Treino associado à ficha de treino

    @ManyToOne(fetch = FetchType.LAZY) // Relacionamento muitos-para-um com WorkoutPlanModel (uma ficha de treino)
    @JoinColumn(name = "workout_plan_id", insertable = false, updatable = false)
    private WorkoutPlanModel workoutPlan; // Ficha de treino associada ao treino

    public WorkoutPerWorkoutPlanModel() {} // Construtor padrão necessário para JPA.

    public WorkoutPerWorkoutPlanModel(WorkoutPerWorkoutPlanId workoutPerWorkoutPlanId, WorkoutModel workout, WorkoutPlanModel workoutPlan) {
        this.workoutPerWorkoutPlanId = workoutPerWorkoutPlanId;
        this.workout = workout;
        this.workoutPlan = workoutPlan;
    }

    public WorkoutPerWorkoutPlanId getWorkoutPerWorkoutPlanId() {
        return workoutPerWorkoutPlanId;
    }

    public void setWorkoutPerWorkoutPlanId(WorkoutPerWorkoutPlanId workoutPerWorkoutPlanId) {
        this.workoutPerWorkoutPlanId = workoutPerWorkoutPlanId;
    }

    public WorkoutModel getWorkout() {
        return workout;
    }

    public void setWorkout(WorkoutModel workout) {
        this.workout = workout;
    }

    public WorkoutPlanModel getWorkoutPlan() {
        return workoutPlan;
    }

    public void setWorkoutPlan(WorkoutPlanModel workoutPlan) {
        this.workoutPlan = workoutPlan;
    }
}
