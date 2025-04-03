package com.gymtutor.gymtutor.activitiesvideos;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/activities-videos")
public class ActivitiesVideosController {

    private final ActivitiesVideosService videosService;

    public ActivitiesVideosController(ActivitiesVideosService videosService) {
        this.videosService = videosService;
    }

    // Criar um novo vídeo associado a um exercício
    @PostMapping("/activity/{activityId}/videos")
    public ResponseEntity<?> createVideo(
            @PathVariable Integer activityId,
            @RequestBody Map<String, String> request
    ) {
        String videoName = request.get("videoName");
        String videoLink = request.get("videoLink");

        // Validação manual (substitui as annotations do DTO)
        if (videoName == null || videoName.isBlank()) {
            return ResponseEntity.badRequest().body("O nome do vídeo não pode estar vazio");
        }
        if (videoName.length() < 3 || videoName.length() > 100) {
            return ResponseEntity.badRequest().body("O nome do vídeo deve ter entre 3 e 100 caracteres");
        }
        if (videoLink == null || videoLink.isBlank()) {
            return ResponseEntity.badRequest().body("O link do vídeo não pode estar vazio");
        }

        try {
            ActivitiesVideosModel newVideo = videosService.createVideo(activityId, videoName, videoLink);
            return ResponseEntity.status(HttpStatus.CREATED).body(newVideo);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Obter vídeos por ID do exercício
    @GetMapping("/activity/{activityId}/videos")
    public ResponseEntity<List<ActivitiesVideosModel>> getVideosByActivityId(
            @PathVariable int activityId
    ) {
        List<ActivitiesVideosModel> videos = videosService.getVideosByActivityId(activityId);
        return ResponseEntity.ok(videos);
    }

    // Atualizar um vídeo
    @PutMapping("/videos/{videoId}")
    public ResponseEntity<?> updateVideo(
            @PathVariable Integer videoId,
            @RequestBody Map<String, String> request
    ) {
        String newName = request.get("videoName");
        String newLink = request.get("videoLink");

        // Validação manual
        if (newName != null && (newName.length() < 3 || newName.length() > 100)) {
            return ResponseEntity.badRequest().body("Nome inválido");
        }
        if (newLink != null && newLink.isBlank()) {
            return ResponseEntity.badRequest().body("Link inválido");
        }

        try {
            ActivitiesVideosModel updatedVideo = videosService.updateVideo(videoId, newName, newLink);
            return ResponseEntity.ok(updatedVideo);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Deletar um vídeo
    @DeleteMapping("/videos/{videoId}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Integer videoId) {
        try {
            videosService.deleteVideo(videoId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}