package com.gymtutor.gymtutor.commonusers.workoutperworkoutplan;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable // Indica que esta classe será usada como uma chave composta em outra entidade.
public class WorkoutPerWorkoutPlanId implements Serializable { // Implementa Serializable para que possa ser usada como chave primária composta.

    @Column(name = "workout_id") // Garantir que o nome da coluna seja "workout_id" e não "workoutId"
    private int workoutId; // Representa o ID do treino na relação N:N.

    @Column(name = "workout_plan_id") // Garantir que o nome da coluna seja "workout_plan_id" e não "workoutplanId"
    private int workoutPlanId; // Representa o ID da ficha de treino na relação N:N.

    public WorkoutPerWorkoutPlanId() {} // Construtor padrão necessário para JPA.

    public WorkoutPerWorkoutPlanId(int workoutId, int workoutPlanId) { // Construtor para inicializar os valores.
        this.workoutId = workoutId;
        this.workoutPlanId = workoutPlanId;
    }

    @Override
    public boolean equals(Object o) {
        // Metodo equals para comparar objetos WorkoutPerWorkoutPlanId corretamente.
        if (this == o) return true; // Se for o mesmo objeto na memória, retorna true.
        if (o == null || getClass() != o.getClass()) return false; // Se o objeto for nulo ou de outra classe, retorna false.
        WorkoutPerWorkoutPlanId that = (WorkoutPerWorkoutPlanId) o;
        return workoutId == that.workoutId && workoutPlanId == that.workoutPlanId; // Compara os IDs para verificar igualdade.
    }

    @Override
    public int hashCode() {
        // Metodo hashCode para garantir que a chave composta funcione corretamente em coleções (ex: HashMap, HashSet).
        return Objects.hash(workoutId, workoutPlanId);
    }

}
