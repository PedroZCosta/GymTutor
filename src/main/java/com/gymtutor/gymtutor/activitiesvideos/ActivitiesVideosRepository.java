package com.gymtutor.gymtutor.activitiesvideos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// ActivitiesVideosRepository.java
@Repository
public interface ActivitiesVideosRepository extends JpaRepository<ActivitiesVideosModel, Integer> {
    List<ActivitiesVideosModel> findByActivity_ActivitiesId(int activity_activitiesId);
}