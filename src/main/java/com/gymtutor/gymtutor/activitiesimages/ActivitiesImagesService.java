package com.gymtutor.gymtutor.activitiesimages;

import com.gymtutor.gymtutor.activities.ActivitiesModel;
import com.gymtutor.gymtutor.activities.ActivitiesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Service
public class ActivitiesImagesService {

    // pegando caminho absoluto (Config WebConfig e UploadProperties + injeção pelo Application.Properties)
    @Value("${upload.dir}")
    private String uploadDir;

    private final ActivitiesImagesRepository imagesRepository;
    private final ActivitiesRepository activitiesRepository; // Repositório dos exercícios

    public ActivitiesImagesService(
            ActivitiesImagesRepository imagesRepository,
            ActivitiesRepository activitiesRepository
    ) {
        this.imagesRepository = imagesRepository;
        this.activitiesRepository = activitiesRepository;
    }

    public ActivitiesImagesModel findById(int imageId) {
        return imagesRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Imagem não encontrada com o ID: " + imageId));
    }

    public Object findByActivityModelActivitiesId(int activitiesId) {
        return imagesRepository.findByActivityActivitiesId(activitiesId);

    }

    public void createImage(@Valid ActivitiesImagesModel activitiesImagesModel, int activitiesId) {
        ActivitiesModel activity = activitiesRepository.findById(activitiesId)
                .orElseThrow(() -> new EntityNotFoundException("Exercícios não Encontrados"));
        activitiesImagesModel.setActivity(activity);
        imagesRepository.save(activitiesImagesModel);
    }

    @Transactional //Se der erro evita exclusão da base de dados
    public void deleteImage(int imageId) {
        ActivitiesImagesModel image = imagesRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Imagem não encontrada"));

        // Remove a imagem física do disco
        String relativePath = image.getImagePath();
        String imageFileName = relativePath.substring(relativePath.lastIndexOf('/') + 1);
        String fullPath = new File(uploadDir, imageFileName).getAbsolutePath();

        // Exclui o arquivo do sistema
        File file = new File(fullPath);
        if (file.exists()) {
            if (!file.delete()) {
                throw new RuntimeException("Erro ao excluir o arquivo físico da imagem");
            }
        }

        // Remove do banco de dados
        imagesRepository.delete(image);
    }

    public void updateImage(int imageId, ActivitiesImagesModel updatedModel) {
        ActivitiesImagesModel existing = imagesRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Imagem não encontrada"));

        existing.setImageName(updatedModel.getImageName());
        if (updatedModel.getImagePath() != null) {
            existing.setImagePath(updatedModel.getImagePath());
        }

        imagesRepository.save(existing);
    }

}
