package com.gymtutor.gymtutor.commonusers.workoutplanperuser;

import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


// A anotação @Repository indica que esta interface é um repositório Spring Data JPA,
// que irá gerenciar a entidade WorkoutPlan, permitindo operações CRUD (criação, leitura, atualização, remoção) no banco de dados.
public interface WorkoutPlanPerUserRepository extends JpaRepository<WorkoutPlanPerUserModel, Integer> {

    // Buscar todos os vínculos por workoutPlanId
    List<WorkoutPlanPerUserModel> findByWorkoutPlanWorkoutPlanId(int workoutPlanId);

    Optional<WorkoutPlanPerUserModel> findByWorkoutPlanPerUserId(WorkoutPlanPerUserId id);

    @Query("SELECT wpum.workoutPlan FROM WorkoutPlanPerUserModel wpum WHERE wpum.user.userId = :userId")
    List<WorkoutPlanModel> findWorkoutPlansByUserId(@Param("userId") int userId);

}
