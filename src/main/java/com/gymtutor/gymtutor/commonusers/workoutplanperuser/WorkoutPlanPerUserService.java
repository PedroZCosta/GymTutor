package com.gymtutor.gymtutor.commonusers.workoutplanperuser;

import com.gymtutor.gymtutor.commonusers.workout.WorkoutModel;
import com.gymtutor.gymtutor.commonusers.workout.WorkoutRepository;
import com.gymtutor.gymtutor.commonusers.workoutactivities.WorkoutActivitiesId;
import com.gymtutor.gymtutor.commonusers.workoutactivities.WorkoutActivitiesModel;
import com.gymtutor.gymtutor.commonusers.workoutactivities.WorkoutActivitiesRepository;
import com.gymtutor.gymtutor.commonusers.workoutexecutionrecordperuser.WorkoutExecutionRecordPerUserService;
import com.gymtutor.gymtutor.commonusers.workoutperworkoutplan.WorkoutPerWorkoutPlanId;
import com.gymtutor.gymtutor.commonusers.workoutperworkoutplan.WorkoutPerWorkoutPlanModel;
import com.gymtutor.gymtutor.commonusers.workoutperworkoutplan.WorkoutPerWorkoutPlanRepository;
import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanModel;
import com.gymtutor.gymtutor.commonusers.workoutplan.WorkoutPlanService;
import com.gymtutor.gymtutor.user.User;
import com.gymtutor.gymtutor.user.UserRepository;
import com.gymtutor.gymtutor.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WorkoutPlanPerUserService {

    @Autowired
    private WorkoutPlanPerUserRepository workoutPlanPerUserRepository;

    @Autowired
    private WorkoutPlanService workoutPlanService;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkoutPerWorkoutPlanRepository workoutPerWorkoutPlanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private WorkoutActivitiesRepository workoutActivitiesRepository;

    @Autowired
    private WorkoutExecutionRecordPerUserService workoutExecutionRecordperUserService;

    // Vincular ficha de treino a um usuario
    @Transactional
    public void linkWorkoutPlanToUser(WorkoutPlanModel workoutPlan, User user){

        WorkoutPlanPerUserId id = new WorkoutPlanPerUserId();
        id.setWorkoutPlanId(workoutPlan.getWorkoutPlanId());
        id.setUserId(user.getUserId());

        WorkoutPlanPerUserModel link = new WorkoutPlanPerUserModel();
        link.setWorkoutPlanPerUserId(id);
        link.setWorkoutPlan(workoutPlan);
        link.setUser(user);

        workoutPlanPerUserRepository.save(link);
    }

    // Desvincular treino da ficha de treino
    @Transactional
    public void unlinkWorkoutPlanPerUser(int workoutPlanId, int userId) {
        var id = new WorkoutPlanPerUserId();
        id.setWorkoutPlanId(workoutPlanId);
        id.setUserId(userId);

        Optional<WorkoutPlanPerUserModel> existingLink = workoutPlanPerUserRepository.findByWorkoutPlanPerUserId(id);

        existingLink.ifPresent(workoutPlanPerUserRepository::delete);
    }

    public WorkoutPlanPerUserModel findById(int WorkoutPlanPerUserId){
        Optional<WorkoutPlanPerUserModel> optionalWorkoutPlanPerUserModel = workoutPlanPerUserRepository.findById(WorkoutPlanPerUserId);
        return optionalWorkoutPlanPerUserModel.orElseThrow(() -> new RuntimeException("workoutPlanPerUser not found with id " + WorkoutPlanPerUserId));
    }

    public List<WorkoutPlanPerUserModel> findAllByWorkoutPlanId(int workoutPlanId) {
        return workoutPlanPerUserRepository.findByWorkoutPlanWorkoutPlanId(workoutPlanId);
    }

    @Transactional
    public void linkUserToPlan(int workoutPlanId, int userId) {
        // Busca o plano original
        WorkoutPlanModel originalPlan = workoutPlanService.findById(workoutPlanId);

        User copiedUser = userRepository.findById(userId).orElseThrow();

        // Clona o plano (sem ID, com novo nome, etc.)
        WorkoutPlanModel clonedPlan = new WorkoutPlanModel();
        clonedPlan.setWorkoutPlanName(originalPlan.getWorkoutPlanName()  );
         // não pode esquecer disso!

        // Descrição
        clonedPlan.setWorkoutPlanDescription(originalPlan.getWorkoutPlanDescription());

        // dias para compleção
        clonedPlan.setTargetDaysToComplete(originalPlan.getTargetDaysToComplete());

        // Você pode definir o user como o mesmo dono do original
        clonedPlan.setUser(originalPlan.getUser()); // ou o novo usuário, se quiser alterar

        // Setando o usuario que ira utilizar essa cópia
        User copyingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário que está copiando não encontrado"));
        clonedPlan.setCopiedForUser(copyingUser);

        // Marca de onde ela foi clonada (caso queira rastrear de qual plano original veio)
        clonedPlan.setClonedFrom(originalPlan);

        workoutPlanService.saveClone(clonedPlan); // SALVA primeiro para gerar ID

        Map<Integer, Integer> originalToClonedWorkoutIds = new HashMap<>();

        // Clona os vínculos com treinos da ficha original
        List<WorkoutPerWorkoutPlanModel> originalWorkoutLinks = originalPlan.getWorkoutPerWorkoutPlans();

        for (WorkoutPerWorkoutPlanModel originalWorkoutLink : originalWorkoutLinks) {
            WorkoutModel originalWorkout = originalWorkoutLink.getWorkout();
            WorkoutModel clonedWorkout = new WorkoutModel();

            // Clona atributos básicos
            clonedWorkout.setWorkoutName(originalWorkout.getWorkoutName());
            clonedWorkout.setRestTime(originalWorkout.getRestTime());
            clonedWorkout.setUser(originalWorkout.getUser());
            clonedWorkout.setReceiverUserId(userId); // seta o usuario como o usuario novo

            // Salva treino clonado
            workoutRepository.save(clonedWorkout);

            // 🔑 Mapeia ID original → ID clonado
            originalToClonedWorkoutIds.put(originalWorkout.getWorkoutId(), clonedWorkout.getWorkoutId());

            // Vínculo do treino clonado com a nova ficha
            WorkoutPerWorkoutPlanModel newLink = new WorkoutPerWorkoutPlanModel();
            WorkoutPerWorkoutPlanId newId = new WorkoutPerWorkoutPlanId();
            newId.setWorkoutId(clonedWorkout.getWorkoutId());
            newId.setWorkoutPlanId(clonedPlan.getWorkoutPlanId());

            newLink.setWorkoutPerWorkoutPlanId(newId);
            newLink.setWorkout(clonedWorkout);
            newLink.setWorkoutPlan(clonedPlan);


            // Clonagem das atividades do treino
            List<WorkoutActivitiesModel> originalActivities = originalWorkout.getWorkoutActivities();

            for (WorkoutActivitiesModel originalActivity : originalActivities) {
                WorkoutActivitiesModel linkedActivity = new WorkoutActivitiesModel();
                linkedActivity.setWorkoutActivitiesId(
                        new WorkoutActivitiesId(clonedWorkout.getWorkoutId(), originalActivity.getActivity().getActivitiesId())
                );
                linkedActivity.setWorkout(clonedWorkout);
                linkedActivity.setActivity(originalActivity.getActivity());
                linkedActivity.setSequence(originalActivity.getSequence());
                linkedActivity.setReps(originalActivity.getReps());

                workoutActivitiesRepository.save(linkedActivity);
            }

            workoutPerWorkoutPlanRepository.save(newLink);
        }


        // Finalmente, vincula o plano clonado ao usuário
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        WorkoutPlanPerUserModel link = new WorkoutPlanPerUserModel();
        WorkoutPlanPerUserId id = new WorkoutPlanPerUserId(clonedPlan.getWorkoutPlanId(), userId);
        link.setWorkoutPlanPerUserId(id);
        link.setWorkoutPlan(clonedPlan);
        link.setUser(user);

        workoutPlanPerUserRepository.save(link);

        // Inicia o acompanhamento de exercicios do aluno
        workoutExecutionRecordperUserService.createInitialCompletedStatusForPlan(link);
        workoutExecutionRecordperUserService.createInitialExecutionsForPlan(link);
    }

    public List<WorkoutPlanModel> findWorkoutPlansByUserId(int userId) {
        return workoutPlanPerUserRepository.findWorkoutPlansByUserId(userId);
    }

}
