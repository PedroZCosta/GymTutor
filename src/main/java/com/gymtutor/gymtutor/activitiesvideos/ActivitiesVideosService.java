package com.gymtutor.gymtutor.activitiesvideos;

import com.gymtutor.gymtutor.activities.ActivitiesModel;
import com.gymtutor.gymtutor.activities.ActivitiesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        return videosRepository.findByActivityModelActivitiesId(activitiesId);

    }

    public void createVideo(@Valid ActivitiesVideosModel activitiesVideosModel, int activitiesId) {
        ActivitiesModel activity = activitiesRepository.findById(activitiesId).orElseThrow(() -> new EntityNotFoundException("Exercícios não Encontrados"));
        activitiesVideosModel.setActivity(activity);
        videosRepository.save(activitiesVideosModel);
    }

    public ActivitiesVideosModel findById(int videoId) {
        Optional<ActivitiesVideosModel> optionalActivitiesVideosModel = videosRepository.findById(videoId);
        return optionalActivitiesVideosModel.orElseThrow(() -> new EntityNotFoundException("Videos não Encontrados"));
    }

    public void deleteVideo(int videoId){
        videosRepository.deleteById(videoId);
    }

    public void updateVideo(ActivitiesVideosModel activitiesVideosModel, int videoId){
        Optional<ActivitiesVideosModel> existingVideo = videosRepository.findById(videoId);
        if(existingVideo.isPresent()){
            ActivitiesVideosModel video = existingVideo.get();

            video.setVideoLink(activitiesVideosModel.getVideoLink());
            video.setVideoName(activitiesVideosModel.getVideoName());

            videosRepository.save(video);
        }
    }
}