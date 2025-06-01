package com.gymtutor.gymtutor.personal.clientperuser;

import com.gymtutor.gymtutor.commonusers.profile.social.UserRelationModel;
import com.gymtutor.gymtutor.commonusers.profile.social.UserRelationRepository;
import com.gymtutor.gymtutor.commonusers.webchat.Conversation;
import org.springframework.ui.Model;
import com.gymtutor.gymtutor.commonusers.profile.social.UserRelationService;
import com.gymtutor.gymtutor.security.CustomUserDetails;
import com.gymtutor.gymtutor.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/personal/linkclients")
public class ClientPerUserController {

    @Autowired
    private ClientPerUserService clientPerUserService;

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private UserRelationRepository userRelationRepository;

    @GetMapping
    public String showClientForm(
            Model model,
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            RedirectAttributes redirectAttributes
            ){
        return handleRequest(redirectAttributes, model, null, null, null, () -> {

            User personal = loggedUser.getUser();

            List<User> connections = userRelationService.getAllConnectedUsers(personal);

            List<UserRelationModel> pendingRelationsSender = userRelationRepository.findBySenderAndAcceptedTrue(loggedUser.getUser());
            List<UserRelationModel> pendingRelationsReceiver = userRelationRepository.findByReceiverAndAcceptedTrue(loggedUser.getUser());


            List<User> unlinkedClientsSender = pendingRelationsSender.stream()
                    .map(UserRelationModel::getReceiver)
                    .toList();


            List<User> unlinkedClientsReceiver = pendingRelationsReceiver.stream()
                    .map(UserRelationModel::getSender)
                    .toList();


            List<User> allUnlinkedClients = new ArrayList<>();
            allUnlinkedClients.addAll(unlinkedClientsReceiver);
            allUnlinkedClients.addAll(unlinkedClientsSender);

            for (User a : allUnlinkedClients) {
                System.err.println(a.getUserEmail());
                System.err.println(a.getUserPassword());
            }

            // Buscar todos os clientes já vinculados a esse personal
            List<ClientPerUserModel> clientLinks = clientPerUserService.findByPersonalId(personal.getUserId());

            // linkedClientIds: IDs dos clientes já vinculados
            List<Integer> linkedClientIds = clientLinks.stream()
                    .map(link -> link.getClient().getUserId())
                    .collect(Collectors.toList());

            // linkedClients: objetos User dos clientes vinculados, ordenados
            List<User> linkedClients = clientLinks.stream()
                    .sorted(Comparator.comparingInt(link -> link.getClient().getUserId()))
                    .map(ClientPerUserModel::getClient)
                    .collect(Collectors.toList());

            model.addAttribute("unlinkedClients", allUnlinkedClients);
            model.addAttribute("connections", connections);
            model.addAttribute("linkedClientIds", linkedClientIds);
            model.addAttribute("linkedClients", linkedClients);
            model.addAttribute("body", "/personal/linkclients/list");
            return "/fragments/layout";
        });
    }

    @PostMapping("/link/{id}")
    public String linkClients(
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            @PathVariable("id") int clientId,
            RedirectAttributes redirectAttributes,
            Model model
            ){
        return handleRequest(redirectAttributes, model, "/personal/linkclients", null, loggedUser, () -> {

            User personal = loggedUser.getUser();

            clientPerUserService.linkClientToPersonal(personal.getUserId(), clientId);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente vinculado com sucesso!");
            return "redirect:/personal/linkclients";
        });
    }

    @PostMapping("/unlink/{id}")
    public String unlinkClients(
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            @PathVariable("id") int clientId,
            RedirectAttributes redirectAttributes,
            Model model
    ){
        return handleRequest(redirectAttributes, model, "/personal/linkclients", null, loggedUser, () ->{

            User personal = loggedUser.getUser();

            clientPerUserService.unlinkClientToPersonal(personal.getUserId(), clientId);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente desvinculado com sucesso!");
            return "redirect:/personal/linkclients";
        });
    }

    private String handleRequest(RedirectAttributes redirectAttributes, Model model, String view, Object clientPerUserModel, Object loggedUser, ClientPerUserController.RequestHandler block) {
        try {
            return block.execute();
        } catch (Exception ex) {
            return handleException(ex, model, view, redirectAttributes, loggedUser);
        }
    }

    @FunctionalInterface
    public interface RequestHandler {
        String execute();
    }

    private String handleException(Exception ex, Model model, String view, RedirectAttributes redirectAttributes, Object loggedUser) {
        return switch (ex) {
            case IllegalArgumentException illegalArgumentException ->
                    handleIllegalArgumentException(illegalArgumentException, model, view, loggedUser);
            case IllegalAccessException illegalAccessException ->
                    handleIllegalAccessException(illegalAccessException, redirectAttributes);
            case DataIntegrityViolationException ignored ->
                    handleDataIntegrityViolationException(model, view, loggedUser);
            case null, default -> handleUnexpectedException(model, view, loggedUser);
        };
    }

    private String handleIllegalArgumentException(IllegalArgumentException ex, Model model, String view, Object loggedUser) {
        return handleError(ex.getMessage(), model, view, loggedUser);
    }

    private String handleIllegalAccessException(IllegalAccessException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/personal/linkclients";
    }

    private String handleDataIntegrityViolationException(Model model, String view, Object loggedUser) {
        return handleError("Erro de integridade de dados.", model, view, loggedUser);
    }

    private String handleUnexpectedException(Model model, String view, Object loggedUser) {
        return handleError("Erro inesperado. Tente novamente.", model, view, loggedUser);
    }

    private String handleError(String errorMessage, Model model, String view, Object loggedUser) {
        if (model != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        if (view != null) {
            assert model != null;
            model.addAttribute("body", view);
            return "/fragments/layout";
        } else {
            return "redirect:/personal/linkclients";
        }
    }

}
