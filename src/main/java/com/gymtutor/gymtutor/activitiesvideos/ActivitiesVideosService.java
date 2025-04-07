package com.gymtutor.gymtutor.activitiesvideos;

import com.gymtutor.gymtutor.activities.ActivitiesModel;
import com.gymtutor.gymtutor.activities.ActivitiesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// ActivitiesVideosService.java
@Service
public class ActivitiesVideosService {

    private final ActivitiesVideosRepository videosRepository;
    private final ActivitiesRepository activitiesRepository; // Repositório dos exercícios

    public ActivitiesVideosService(
            ActivitiesVideosRepository videosRepository,
            ActivitiesRepository activitiesRepository
    ) {
        this.videosRepository = videosRepository;
        this.activitiesRepository = activitiesRepository;
    }

    public Object findByActivityModelActivitiesId(int activitiesId) {
        return videosRepository.findByActivityModel_ActivitiesId(activitiesId);

    }

    public void createVideo(@Valid ActivitiesVideosModel activitiesVideosModel, int activitiesId) {
        activitiesRepository.findById(activitiesId).orElseThrow(() -> new EntityNotFoundException("Activities not found"));
        activitiesVideosModel.setActivity(activitiesRepository.findById(activitiesId).get());
        videosRepository.save(activitiesVideosModel);
    }

    public ActivitiesVideosModel findById(int videoId) {
        Optional<ActivitiesVideosModel> optionalActivitiesVideosModel = videosRepository.findById(videoId);
        return optionalActivitiesVideosModel.orElseThrow(() -> new EntityNotFoundException("Video not found"));
    }
    public List<ActivitiesVideosModel> findAll(){
        return videosRepository.findAll();
    }
    public void deleteVideo(int videoId){
        videosRepository.deleteById(videoId);
    }
    public void updateVideo(ActivitiesVideosModel activitiesVideosModel, int videoId, int activitiesId){
        Optional<ActivitiesVideosModel> existingVideo = videosRepository.findById(videoId);
        if(existingVideo.isPresent()){
            ActivitiesVideosModel video = existingVideo.get();

            video.setVideoLink(activitiesVideosModel.getVideoLink());
            video.setVideoName(activitiesVideosModel.getVideoName());

            videosRepository.save(video);
        }
    }
}