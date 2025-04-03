package com.gymtutor.gymtutor.commonusers.workoutactivities;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable // Indica que esta classe será usada como uma chave composta em outra entidade.
class WorkoutActivitiesId implements Serializable { // Implementa Serializable para que possa ser usada como chave primária composta.

    @Column(name = "workout_id") // Garantir que o nome da coluna seja "workout_id" e não "workoutId"
    private int workoutId; // Representa o ID do treino na relação N:N.

    @Column(name = "activity_id") // Garantir que o nome da coluna seja "workout_id" e não "workoutId"
    private int activityId; // Representa o ID da atividade na relação N:N.

    public WorkoutActivitiesId() {} // Construtor padrão necessário para JPA.

    public WorkoutActivitiesId(int workoutId, int activityId) { // Construtor para inicializar os valores.
        this.workoutId = workoutId;
        this.activityId = activityId;
    }

    @Override
    public boolean equals(Object o) {
        // Metodo equals para comparar objetos WorkoutActivitiesId corretamente.
        if (this == o) return true; // Se for o mesmo objeto na memória, retorna true.
        if (o == null || getClass() != o.getClass()) return false; // Se o objeto for nulo ou de outra classe, retorna false.
        WorkoutActivitiesId that = (WorkoutActivitiesId) o;
        return workoutId == that.workoutId && activityId == that.activityId; // Compara os IDs para verificar igualdade.
    }

    @Override
    public int hashCode() {
        // Metodo hashCode para garantir que a chave composta funcione corretamente em coleções (ex: HashMap, HashSet).
        return Objects.hash(workoutId, activityId);
    }
}
