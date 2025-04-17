package com.gymtutor.gymtutor.commonusers.workoutactivities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// A anotação @Repository indica que esta interface é um repositório Spring Data JPA,
// que irá gerenciar a entidade WorkoutActivities, permitindo operações CRUD (criação, leitura, atualização, remoção) no banco de dados.
@Repository
public interface WorkoutActivitiesRepository extends JpaRepository<WorkoutActivitiesModel, Integer> {


    // Seguindo a convenção do JPA Optional<Entity> findByIdType(IdType id);
    Optional<WorkoutActivitiesModel> findByWorkoutActivitiesId(WorkoutActivitiesId id);

    @Query("SELECT a.activitiesId FROM ActivitiesModel a")
    List<Integer> findAllIds();
}
