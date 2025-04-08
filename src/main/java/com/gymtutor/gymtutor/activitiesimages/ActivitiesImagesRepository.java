package com.gymtutor.gymtutor.activitiesimages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivitiesImagesRepository extends JpaRepository<ActivitiesImagesModel, Integer> {

    List<ActivitiesImagesModel> findByActivityActivitiesId(int activitiesId);

}
