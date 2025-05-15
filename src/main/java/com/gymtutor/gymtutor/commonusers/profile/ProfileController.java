package com.gymtutor.gymtutor.commonusers.profile;

import com.gymtutor.gymtutor.security.CustomUserDetails;
import com.gymtutor.gymtutor.security.SecurityUtils;
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
import java.util.Objects;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Value("{upload.dir.base}/users")
    private String uploadDirUsers;

    @Autowired
    private PersonalService personalService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;

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

                // enviando uma mensagem do Status do CREF ao front
                if (!personal.isApproved() && !personal.isRejected()) {
                    model.addAttribute("crefMessage", "CREF em análise pelos administradores do sistema");
                } else if (personal.isApproved()) {
                    model.addAttribute("crefMessage", "CREF Válido");
                } else if (personal.isRejected()) {
                    model.addAttribute("crefMessage", "Seu CREF foi Rejeitado, Motivo: " + personal.getRejectReason());
                }
            }
            if (!model.containsAttribute("changeNameForm")) {
                model.addAttribute("changeNameForm", new ChangeNameDTO());
            }



            model.addAttribute("personalCrefDTO", new PersonalCrefDTO());
            model.addAttribute("states", State.values());
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

        return handleRequest(redirectAttributes, model, "profile/profile", null, () -> {
            User user = loggedUser.getUser();
            userService.changePassword(user, userChangePasswordDTO.getUserPassword());
            redirectAttributes.addFlashAttribute("successMessage", "Sua senha foi alterada com sucesso!");
            return "redirect:/profile";
        });
    }

    @Transactional
    @PostMapping("/save-cref")
    public String saveCref(
            @Valid @ModelAttribute PersonalCrefDTO personalCrefDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails loggedUser,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", loggedUser.getUser());
            model.addAttribute("personal", personalService.findByUser(loggedUser.getUser())); // acho que essa lógica e desnecessária
            model.addAttribute("changeNameForm", new ChangeNameDTO());
            model.addAttribute("states", State.values());
            model.addAttribute("personalCrefDTO", personalCrefDTO);
            model.addAttribute("openCrefModal", true); // sinalizador
            model.addAttribute("org.springframework.validation.BindingResult.personalCrefDTO", bindingResult);
            model.addAttribute("body", "profile/profile");
            return "/fragments/layout";
        }

        return handleRequest(redirectAttributes, model, "profile/profile", null, () -> {

            User user = userService.findById(loggedUser.getUser().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

            // Cria um novo Personal e vincula ao usuário
            Personal personal = new Personal();
            personal.setPersonalCREF(personalCrefDTO.getPersonalCREF());
            personal.setState(personalCrefDTO.getState());
            personal.setUser(user);
            personalService.save(personal);

//            userService.changeRole(user, RoleName.PERSONAL);

            // Atualiza o papel no CustomUserDetails
            loggedUser.getUser().setRole(roleRepository.findByRoleName(RoleName.PERSONAL));
            SecurityUtils.updateAuthenticatedUser(loggedUser);

            redirectAttributes.addFlashAttribute("successMessage", "CREF cadastrado com sucesso!");
            return "redirect:/profile";

        });
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

            // Atualiza o papel no CustomUserDetails
            user.setRole(roleRepository.findByRoleName(RoleName.STUDENT));
            SecurityUtils.updateAuthenticatedUser(loggedUser);


            redirectAttributes.addFlashAttribute("successMessage", "Seu CREF foi removido com sucesso!");
            return "redirect:/profile";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Senha incorreta. CREF não removido.");
            return "redirect:/profile";
        }
    }

    @PostMapping("/change-name")
    public String changeName(
            @Valid @ModelAttribute("changeNameForm") ChangeNameDTO changeNameDTO,
            BindingResult result,

            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails loggedUser
    ){
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            return "redirect:/profile";
        }

        User user = userService.findById(loggedUser.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (userService.checkPassword(user, changeNameDTO.getConfirmPassword3())) {
            userService.changeName(user, changeNameDTO.getNewName());
            loggedUser.getUser().setUserName(changeNameDTO.getNewName());
            SecurityUtils.updateAuthenticatedUser(loggedUser);
            redirectAttributes.addFlashAttribute("successMessage", "Seu nome foi alterado com sucesso!");
            return "redirect:/profile";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Senha incorreta. Não foi possível alterar o nome");
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
        return "redirect:/home";
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
            return "redirect:/profile";
        }
    }

}
