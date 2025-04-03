package com.gymtutor.gymtutor.activitiesvideos;

import com.gymtutor.gymtutor.activities.ActivitiesModel;
import com.gymtutor.gymtutor.activities.ActivitiesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // Criar um novo vídeo associado a um exercício
    public ActivitiesVideosModel createVideo(Integer activityId, String videoName, String videoLink) {
        ActivitiesModel activity = activitiesRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Exercício não encontrado!"));

        ActivitiesVideosModel video = new ActivitiesVideosModel();
        video.setVideoName(videoName);
        video.setVideoLink(videoLink);
        video.setActivity(activity);

        return videosRepository.save(video);
    }

    public ActivitiesVideosModel updateVideo(Integer videoId, String newName, String newLink) {
        ActivitiesVideosModel video = videosRepository.findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Vídeo não encontrado!"));

        if (newName != null) video.setVideoName(newName);
        if (newLink != null) video.setVideoLink(newLink);

        return videosRepository.save(video);
    }

    // Buscar vídeos por ID do exercício
    public List<ActivitiesVideosModel> getVideosByActivityId(int activityId) {
        return videosRepository.findByActivity_ActivitiesId(activityId);
    }

    // Atualizar um vídeo
    public ActivitiesVideosModel updateVideo(Integer videoId, String newLink) {
        ActivitiesVideosModel video = videosRepository.findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Vídeo não encontrado!"));

        video.setVideoLink(newLink);
        return videosRepository.save(video);
    }

    // Deletar um vídeo
    public void deleteVideo(Integer videoId) {
        videosRepository.deleteById(videoId);
    }
}