package com.gymtutor.gymtutor.commonusers.workout;

import com.gymtutor.gymtutor.activities.ActivitiesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// A anotação @Repository indica que esta interface é um repositório Spring Data JPA,
// que irá gerenciar a entidade Workout, permitindo operações CRUD (criação, leitura, atualização, remoção) no banco de dados.
@Repository
public interface WorkoutRepository extends JpaRepository<WorkoutModel, Integer>{
}
