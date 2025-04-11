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

    // Buscar todos os vínculos por workoutId
    List<WorkoutPerWorkoutPlanModel> findByWorkoutWorkoutId(int workoutId);

    // Buscar todos os vínculos por workoutPlanId
    List<WorkoutPerWorkoutPlanModel> findByWorkoutPlanWorkoutPlanId(int workoutPlanId);


    // Buscar vínculo específico entre workout e workoutPlan
    // Nome customizado para seguir o padrão do projeto sem underline.
    @Query("SELECT w FROM WorkoutPerWorkoutPlanModel w WHERE w.workout.workoutId = :workoutId AND w.workoutPlan.workoutPlanId = :workoutPlanId")
    Optional<WorkoutPerWorkoutPlanModel> findByWorkoutIdAndWorkoutPlanId(
            @Param("workoutId") int workoutId,
            @Param("workoutPlanId") int workoutPlanId
    ); // todo: acabar com essa query personalizada e utilizar a nomeclatura correta
}
