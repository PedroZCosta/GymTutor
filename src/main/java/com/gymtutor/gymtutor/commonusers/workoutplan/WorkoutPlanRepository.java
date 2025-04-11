package com.gymtutor.gymtutor.commonusers.workoutplan;

import com.gymtutor.gymtutor.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// A anotação @Repository indica que esta interface é um repositório Spring Data JPA,
// que irá gerenciar a entidade WorkoutPlan, permitindo operações CRUD (criação, leitura, atualização, remoção) no banco de dados.
@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlanModel, Integer> {
    List<WorkoutPlanModel> findAllByUser(User user); // Busca todas as fichas de treino associadas ao usuario

}
