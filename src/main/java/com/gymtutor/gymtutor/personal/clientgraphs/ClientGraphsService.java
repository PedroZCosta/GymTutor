package com.gymtutor.gymtutor.personal.clientgraphs;

import com.gymtutor.gymtutor.commonusers.workoutexecutionrecordperuser.WorkoutExecutionRecordPerUserModel;
import com.gymtutor.gymtutor.commonusers.workoutexecutionrecordperuser.WorkoutExecutionRecordPerUserService;
import com.gymtutor.gymtutor.commonusers.workoutexecutionrecordperuser.WorkoutPlanWithRecordsDTO;
import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanModel;
import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanService;
import com.gymtutor.gymtutor.personal.clientperuser.ClientPerUserModel;
import com.gymtutor.gymtutor.personal.clientperuser.ClientPerUserService;
import com.gymtutor.gymtutor.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientGraphsService {

    @Autowired
    private ClientPerUserService clientPerUserService;

    @Autowired
    private WorkoutExecutionRecordPerUserService workoutExecutionRecordPerUserService;

    @Autowired
    private WorkoutPlanService workoutPlanService;

    public List<ClientGraphDTO> findAllClientsGraphs(int personalId){
        // instaciamento da lista de DTO
        List<ClientGraphDTO> listClientGraphDTOs = new ArrayList<>();


        // Recupera todos os clientes por pesonal logado
        List<ClientPerUserModel> allCLientsPerPersonalModelPaia = clientPerUserService.findByPersonalId(personalId);
        ArrayList<User> allClients = new ArrayList<>();
        // popula o allClients com as models dos usuarios
        for (ClientPerUserModel clientPerUserModel : allCLientsPerPersonalModelPaia) {
            allClients.add(clientPerUserModel.getClient());
        }

        for (User user : allClients) {
            ClientGraphDTO clientGraphDTO = new ClientGraphDTO();
            clientGraphDTO.setUser(user);

            var allRecords = workoutExecutionRecordPerUserService.findAllRecordByPersonalId(user.getUserId());

            var groupedByWorkoutPlan = allRecords.stream()
                    .collect(Collectors.groupingBy(record -> record.getWorkoutExecutionRecordPerUserId().getWorkoutPlanId()));

            List<WorkoutPlanWithRecordsDTO> workoutPlanDTOs = new ArrayList<>();

            for (var entry : groupedByWorkoutPlan.entrySet()) {
                Integer workoutPlanId = entry.getKey();
                List<WorkoutExecutionRecordPerUserModel> records = entry.getValue();

                WorkoutPlanWithRecordsDTO dto = new WorkoutPlanWithRecordsDTO();
                WorkoutPlanModel workoutPlan = workoutPlanService.findById(workoutPlanId);
                dto.setWorkoutPlan(workoutPlan);
                dto.setRecords(records);
                workoutPlanDTOs.add(dto);
            }

            clientGraphDTO.setWorkoutPlanWithRecordsList(workoutPlanDTOs);
            listClientGraphDTOs.add(clientGraphDTO);
        }

        // usuarios aqui e daqui eu consigo os id dos usuarios que dai eu consigo
        return listClientGraphDTOs;

    }
}
