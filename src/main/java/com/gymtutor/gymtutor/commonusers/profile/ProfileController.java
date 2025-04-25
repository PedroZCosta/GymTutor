package com.gymtutor.gymtutor.commonusers.profile;

import com.gymtutor.gymtutor.security.CustomUserDetails;
import com.gymtutor.gymtutor.user.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Value("{upload.dir.base}/users")
    private String uploadDirUsers;

    @Autowired
    private PersonalService personalService;
    @Autowired
    private UserService userService;

    // TODO: FAZER TODOS OS handlerequests  e bidingerros,
    //  falta criar a remoção do cref para o usuario voltar a ser apenas Aluno
    // validar se a imagem e jpg??
    @GetMapping
    public String showProfile(
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails loggedUser
    ) {
        return handleRequest(redirectAttributes, model, null, null, () -> {
            User user = loggedUser.getUser();
            model.addAttribute("user", user);
            Personal personal = personalService.findByUser(user);
            if (personal != null) {
                model.addAttribute("personal", personal);
            }
            model.addAttribute("body", "profile/profile");
            return "/fragments/layout";
        });
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("userChangePasswordDTO", new UserChangePasswordDTO());
        model.addAttribute("body", "profile/change-password");
        return "/fragments/layout";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @Valid @ModelAttribute UserChangePasswordDTO userChangePasswordDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails loggedUser
    ) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Há erros no formulário!");
            model.addAttribute("org.springframework.validation.BindingResult.userChangePasswordDTO", bindingResult);
            model.addAttribute("userChangePasswordDTO", userChangePasswordDTO);
            model.addAttribute("body", "profile/change-password");
            return "/fragments/layout";
        }
        //TODO: Adicionar a validação de erros ao salvar a senha
        User user = loggedUser.getUser();
        userService.changePassword(user, userChangePasswordDTO.getUserPassword());
        redirectAttributes.addFlashAttribute("successMessage", "Sua senha foi alterada com sucesso!");
        return "redirect:/profile";
    }

    @Transactional
    @PostMapping("/save-creef")
    public String saveCreef(
            @RequestParam("personalCREEF") String creef,
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            RedirectAttributes redirectAttributes
    ) {
        User user = userService.findById(loggedUser.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Cria um novo Personal e vincula ao usuário
        Personal personal = new Personal();
        personal.setPersonalCREEF(creef);
        personal.setUser(user);
        personalService.save(personal);

        // Troca o papel para PERSONAL
        userService.changeRole(user, RoleName.PERSONAL);

        redirectAttributes.addFlashAttribute("successMessage", "CREEF cadastrado com sucesso!, logue novamente para acessar as funções de Personal!");
        return "redirect:/profile";
    }


    @PostMapping("/upload-photo")
    public String uploadPhoto(
            @RequestParam("image") MultipartFile image,
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            RedirectAttributes redirectAttributes
    ) {
        User user = loggedUser.getUser();

        try {
            Path userDir = Paths.get("uploads/images/users", String.valueOf(user.getUserId()));
            Files.createDirectories(userDir);
            Path filePath = userDir.resolve("profile.jpg");

            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            redirectAttributes.addFlashAttribute("successMessage", "Foto do perfil alterada com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao carregar a imagem.");
        }

        return "redirect:/profile";
    }



    @PostMapping("/disable-account")
    public String disableAccount(
            @RequestParam("confirmPassword") String password,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails loggedUser
    ) {
        User user = loggedUser.getUser();

        if (userService.checkPassword(user, password)) {
            userService.disableUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Sua conta foi desativada com sucesso!");
            return "redirect:/login"; 
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Senha incorreta. Conta não desativada.");
            return "redirect:/profile";
        }
    }

    @Transactional
    @PostMapping("/remove-cref")
    public String removeCref(
            @RequestParam("confirmPassword2") String password,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails loggedUser
    ) {
        User user = loggedUser.getUser();

        if (userService.checkPassword(user, password)) {
            personalService.delete(user);
            userService.changeRole(user, RoleName.STUDENT);
            redirectAttributes.addFlashAttribute("successMessage", "Seu CREF foi removido com sucesso!, logue novamente!");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Senha incorreta. CREF não removido.");
            return "redirect:/profile";
        }
    }


    private String handleRequest(RedirectAttributes redirectAttributes, Model model, String view, Object anyModel, RequestHandler block
    ){
        try{
            return block.execute();
        }catch (Exception ex) {
            return handleException(ex, model, anyModel, view, redirectAttributes);
        }
    }

    @FunctionalInterface
    public interface RequestHandler{
        String execute();
    }

    private String handleException(Exception ex, Model model, Object anyModel, String view, RedirectAttributes redirectAttributes){
        return switch (ex) {
            case IllegalArgumentException illegalArgumentException ->
                    handleIllegalArgumentException(illegalArgumentException, model, anyModel, view);
            case IllegalAccessException illegalAccessException ->
                    handleIllegalAccessException(illegalAccessException, redirectAttributes);
            case DataIntegrityViolationException ignored ->
                    handleDataIntegrityViolationException(model, anyModel, view);
            case null, default -> handleUnexpectedException(model, anyModel, view);
        };
    }

    private String handleIllegalArgumentException(IllegalArgumentException ex, Model model, Object anyModel, String view){
        return handleError(ex.getMessage(), model, anyModel, view);
    }

    private String handleIllegalAccessException(IllegalAccessException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/admin/activities";
    }

    private String handleDataIntegrityViolationException(Model model, Object anyModel, String view){
        return handleError("Erro de integridade de dados.", model, anyModel, view);
    }

    private String handleUnexpectedException(Model model, Object anyModel, String view){
        return handleError("Erro inesperado. Tente novamente.", model, anyModel, view);

    }

    private String handleError(String errorMessage, Model model, Object anyModel, String view){
        if (model != null) {
            model.addAttribute("errorMessage", errorMessage);
            if (anyModel instanceof UserRegistrationDTO) {
                model.addAttribute("model", anyModel);
            } else if (anyModel instanceof UserRecoveryPasswordDTO) {
                model.addAttribute("model", anyModel);
            }
        }
        if(view != null){
            assert model != null;
            model.addAttribute("body", view);
            return "/fragments/layout";
        }else{
            return "redirect:/admin/activities";
        }
    }

}
