package com.gymtutor.gymtutor.commonusers.profile.social;

import com.gymtutor.gymtutor.activitiesvideos.ActivitiesVideosController;
import com.gymtutor.gymtutor.activitiesvideos.ActivitiesVideosModel;
import com.gymtutor.gymtutor.security.CustomUserDetails;
import com.gymtutor.gymtutor.user.User;
import com.gymtutor.gymtutor.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/student/social")
public class UserRelationController {

    @Autowired
    private UserRelationService relationService;

    @Autowired
    private UserService userService;

    // TODO criar as validações de erros

    @GetMapping
    public String listConnections(
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails loggedUser
    ) {
        return handleRequest(redirectAttributes, model, null, null, null, () -> {

            User user = loggedUser.getUser();

            List<User> connections = relationService.getAllConnectedUsers(user);
            List<UserRelationModel> pendingRequests = relationService.getPendingRequests(user);

            model.addAttribute("connections", connections);
            model.addAttribute("pendingRequests", pendingRequests);
            model.addAttribute("body", "/student/social/list");
            return "/fragments/layout";
        });
    }

    @GetMapping("/search")
    public String searchUsers(
            @RequestParam(name = "query", required = false) String query,
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails loggedUser
    ) {

        User user = loggedUser.getUser();

        List<User> results = new ArrayList<>();

        if (query != null && !query.isBlank()) {
            results = userService.searchUsersByName(query, user);
        }

        Map<Integer, RelationStatus> relationMap = relationService.mapUserRelationStatus(user, results);

        model.addAttribute("results", results);
        model.addAttribute("relationMap", relationMap);
        model.addAttribute("query", query);
        model.addAttribute("body", "/student/social/search");
        return "/fragments/layout";
    }


    @PostMapping("/connect/{id}")
    public String sendConnectionRequest(
            @PathVariable("id") int receiverId,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            HttpServletRequest request
    ) {
        User sender = loggedUser.getUser();
        User receiver = userService.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Erro ao cadastrar usuário não encontrado"));

        if (sender.getUserId() == receiverId) {
            redirectAttributes.addFlashAttribute("errorMessage", "Você não pode enviar conexão para si mesmo.");
            return "redirect:/student/social/search";
        }

        Optional<UserRelationModel> existing = relationService.findRelation(sender.getUserId(), receiverId);

        if (existing.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Conexão já existe ou está pendente.");
        } else {
            UserRelationModel relation = new UserRelationModel();
            relation.setId(new UserRelationKey(sender.getUserId(), receiverId));
            relation.setSender(sender);
            relation.setReceiver(receiver);
            relation.setAccepted(false);
            relationService.save(relation);

            redirectAttributes.addFlashAttribute("successMessage", "Solicitação de conexão enviada!");
        }


        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/student/social/search");
    }


    @PostMapping("/accept/{id}")
    public String acceptConnection(
            @PathVariable("id") int senderId,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            HttpServletRequest request
    ) {

        Optional<UserRelationModel> relationOpt = relationService.findRelation(senderId, loggedUser.getUserId());

        if (relationOpt.isPresent()) {
            UserRelationModel relation = relationOpt.get();
            relation.setAccepted(true);
            relationService.save(relation);

            redirectAttributes.addFlashAttribute("successMessage", "Conexão aceita com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Solicitação de conexão não encontrada.");
        }

        String referer = request.getHeader("Referer");
        //noinspection SpringMVCViewInspection
        return "redirect:" + (referer != null ? referer : "/student/social");
    }


    @PostMapping("/reject/{id}")
    public String rejectConnection(
            @PathVariable("id") int senderId,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            HttpServletRequest request
    ) {

        Optional<UserRelationModel> relationOpt = relationService.findRelation(senderId, loggedUser.getUserId());

        if (relationOpt.isPresent()) {
            relationService.delete(relationOpt.get());
            redirectAttributes.addFlashAttribute("successMessage", "Conexão rejeitada com sucesso.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Solicitação de conexão não encontrada.");
        }

        String referer = request.getHeader("Referer");
        //noinspection SpringMVCViewInspection
        return "redirect:" + (referer != null ? referer : "/student/social");
    }


    @PostMapping("/cancel/{id}")
    public String cancelSentRequest(
            @PathVariable("id") int receiverId,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            HttpServletRequest request
    ) {
        User sender = loggedUser.getUser();
        Optional<UserRelationModel> relationOpt = relationService.findRelation(sender.getUserId(), receiverId);

        if (relationOpt.isPresent() && !relationOpt.get().isAccepted()) {
            relationService.delete(relationOpt.get());
            redirectAttributes.addFlashAttribute("successMessage", "Solicitação de conexão cancelada.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Não foi possível cancelar a solicitação.");
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/student/social/search");
    }


    @PostMapping("/unfriend/{id}")
    public String unfriendUser(
            @PathVariable("id") int otherUserId,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            HttpServletRequest request
    ) {
        Optional<UserRelationModel> relationOpt =
                relationService.findRelation(loggedUser.getUserId(), otherUserId);

        if (relationOpt.isEmpty()) {
            relationOpt = relationService.findRelation(otherUserId, loggedUser.getUserId());
        }

        if (relationOpt.isPresent() && relationOpt.get().isAccepted()) {
            relationService.delete(relationOpt.get());
            redirectAttributes.addFlashAttribute("successMessage", "Conexão desfeita com sucesso.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Conexão não encontrada.");
        }

        String referer = request.getHeader("Referer");
        //noinspection SpringMVCViewInspection
        return "redirect:" + (referer != null ? referer : "/student/social");
    }


    private String handleRequest(RedirectAttributes redirectAttributes, Model model, String view, ActivitiesVideosModel activitiesVideosModel, Integer activitiesId, ActivitiesVideosController.RequestHandler block
    ){
        try{
            return block.execute();
        }catch (Exception ex) {
            return "redirect:/home";
        }
    }

    @FunctionalInterface
    public interface RequestHandler{
        String execute();
    }
}