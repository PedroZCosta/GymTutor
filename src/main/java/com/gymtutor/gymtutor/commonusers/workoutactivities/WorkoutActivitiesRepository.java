package com.gymtutor.gymtutor.commonusers.workoutactivities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// A anotação @Repository indica que esta interface é um repositório Spring Data JPA,
// que irá gerenciar a entidade WorkoutActivities, permitindo operações CRUD (criação, leitura, atualização, remoção) no banco de dados.
@Repository
public interface WorkoutActivitiesRepository extends JpaRepository<WorkoutActivitiesModel, Integer> {
}
