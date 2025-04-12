package com.gymtutor.gymtutor.commonusers.workoutplanperuser;

import com.gymtutor.gymtutor.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


// A anotação @Repository indica que esta interface é um repositório Spring Data JPA,
// que irá gerenciar a entidade WorkoutPlan, permitindo operações CRUD (criação, leitura, atualização, remoção) no banco de dados.
public interface WorkoutPlanPerUserRepository extends JpaRepository<WorkoutPlanPerUserModel, Integer> {

    // Buscar todos os vínculos por workoutPlanId
    List<WorkoutPlanPerUserModel> findByWorkoutPlanWorkoutPlanId(int workoutPlanId);

    // Buscar todos os vínculos por workoutId
    List<User> findByUserUserId(int userId);

    Optional<WorkoutPlanPerUserModel> findByUserUserIdAndWorkoutPlanWorkoutPlanId(Integer userId, Integer workoutPlanId);

    Optional<WorkoutPlanPerUserModel> findByWorkoutPlanPerUserId(WorkoutPlanPerUserId id);

}
