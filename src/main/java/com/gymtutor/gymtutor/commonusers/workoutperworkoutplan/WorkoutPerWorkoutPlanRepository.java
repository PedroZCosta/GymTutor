package com.gymtutor.gymtutor.commonusers.workoutperworkoutplan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// A anotação @Repository indica que esta interface é um repositório Spring Data JPA,
// que irá gerenciar a entidade WorkoutPlan, permitindo operações CRUD (criação, leitura, atualização, remoção) no banco de dados.
@Repository
public interface WorkoutPerWorkoutPlanRepository extends JpaRepository<WorkoutPerWorkoutPlanModel, Integer> {

    // Buscar vínculo específico entre workout e workoutPlan
    Optional<WorkoutPerWorkoutPlanModel> findByWorkout_WorkoutIdAndWorkoutPlan_WorkoutPlanId(int workoutId, int workoutPlanId);

}
