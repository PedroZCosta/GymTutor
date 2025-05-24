package com.gymtutor.gymtutor.commonusers.workoutactivities;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable // Indica que esta classe será usada como uma chave composta em outra entidade.
public class WorkoutActivitiesId implements Serializable { // Implementa Serializable para que possa ser usada como chave primária composta.

    @Column(name = "workout_id") // Garantir que o nome da coluna seja "workout_id" e não "workoutId"
    private int workoutId; // Representa o ID do treino na relação N:N.

    @Column(name = "activity_id") // Garantir que o nome da coluna seja "workout_id" e não "workoutId"
    private int activitiesId; // Representa o ID da atividade na relação N:N.

    public WorkoutActivitiesId() {} // Construtor padrão necessário para JPA.

    public WorkoutActivitiesId(int workoutId, int activitiesId) { // Construtor para inicializar os valores.
        this.workoutId = workoutId;
        this.activitiesId = activitiesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutActivitiesId that = (WorkoutActivitiesId) o;
        return Objects.equals(workoutId, that.workoutId) &&
                Objects.equals(activitiesId, that.activitiesId);
    }

    @Override
    public int hashCode() {
        // Metodo hashCode para garantir que a chave composta funcione corretamente em coleções (ex: HashMap, HashSet).
        return Objects.hash(workoutId, activitiesId);
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public int getActivitiesId() {
        return activitiesId;
    }

    public void setActivitiesId(int activitiesId) {
        this.activitiesId = activitiesId;
    }
}
