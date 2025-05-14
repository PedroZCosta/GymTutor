package com.gymtutor.gymtutor.admin.activities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Serviço para gerenciar a lógica de negócios relacionada a entidades clínicas (ActivitiesService),
// incluindo operações de criação, atualização, exclusão e recuperação de entidades,
// além de realizar validações de permissão de acesso para o usuário logado.
@Service
public class ActivitiesService {

    @Autowired
    private ActivitiesRepository activitiesRepository;

    public void createActivity(ActivitiesModel activitiesModel){
        activitiesRepository.save(activitiesModel);
    }

    public ActivitiesModel findById(int activitiesId){
        Optional<ActivitiesModel> optionalActivitiesModel = activitiesRepository.findById(activitiesId);
        return optionalActivitiesModel.orElseThrow(()-> new RuntimeException("Atividade não encontrada com o ID: "+ activitiesId));
    }

    public List<ActivitiesModel> findAll(){
            return activitiesRepository.findAll();
    }

    public void updateActivity(ActivitiesModel activitiesModel, int activitiesId) {
        // Busca a atividade existente pelo ID
        Optional<ActivitiesModel> existingActivity = activitiesRepository.findById(activitiesId);

        // Se a atividade for encontrada, atualiza os dados
        if (existingActivity.isPresent()) {
            ActivitiesModel activity = existingActivity.get();

            // Atualiza os campos da atividade
            activity.setActivityName(activitiesModel.getActivityName());
            activity.setActivityDescription(activitiesModel.getActivityDescription());
            activity.setMuscularGroup(activitiesModel.getMuscularGroup());

            // Salva a atividade atualizada no banco de dados
            activitiesRepository.save(activity);
        }
    }
    public void deleteActivities(int activitiesId){
        activitiesRepository.deleteById(activitiesId);

    }

}
