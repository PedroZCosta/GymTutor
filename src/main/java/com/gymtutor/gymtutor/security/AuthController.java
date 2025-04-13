package com.gymtutor.gymtutor.security;

import com.gymtutor.gymtutor.user.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login() {

        return "login";
    }


    //TODO: IMPLEMENTAR MÉTODOS DE CADASTRO DOS USUÁRIOS

    // Metodo para registrar um novo usuário
    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userRegistrationDTO", new UserRegistrationDTO());
        return "registration";
    }

    @PostMapping("/registration")
    public String processRegistration(
            @Valid @ModelAttribute UserRegistrationDTO userRegistrationDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()){
            model.addAttribute("errorMessage", "Há erros no formulário!");
            model.addAttribute("org.springframework.validation.BindingResult.userRegistrationDTO", bindingResult);
            return "/registration";
        }

        return handleRequest(redirectAttributes, model, "registration", userRegistrationDTO, () -> {

            userService.createUser(userRegistrationDTO);
            redirectAttributes.addFlashAttribute("successMessage","Contra criada com sucesso!!!");
            return "redirect:/login";
        });
    }

    @GetMapping("/password-recovery")
    public String showRecoveryForm(Model model) {
        model.addAttribute("userRecoveryPasswordDTO", new UserRecoveryPasswordDTO());
        return "password-recovery";
    }


    @PostMapping("/password-recovery")
    public String processPasswordRecovery(
            @Valid @ModelAttribute UserRecoveryPasswordDTO userRecoveryPasswordDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()){
            model.addAttribute("errorMessage", "Há erros no formulário!");
            model.addAttribute("org.springframework.validation.BindingResult.userRecoveryPasswordDTO", bindingResult);
            return "/password-recovery";
        }
        return handleRequest(redirectAttributes, model, "password-recovery", userRecoveryPasswordDTO, () -> {
            // TODO: incluir a lógica para recuperação de senha
            User user = userRepository.findByUserEmail(userRecoveryPasswordDTO.getUserEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
            // Reativa o usuário ao solicitar recuperação de senha
            userService.enableUser(user);
            redirectAttributes.addFlashAttribute("successMessage","Um E-mail foi enviado para a sua conta!");
            return "redirect:/login";
        });
    }


    private String handleRequest(RedirectAttributes redirectAttributes, Model model, String view, Object anyDTO, RequestHandler block
    ){
        try{
            return block.execute();
        }catch (Exception ex) {
            return handleException(ex, model, anyDTO, view, redirectAttributes);
        }
    }

    @FunctionalInterface
    public interface RequestHandler{
        String execute();
    }

    private String handleException(Exception ex, Model model, Object anyDTO, String view, RedirectAttributes redirectAttributes){
        return switch (ex) {
            case IllegalArgumentException illegalArgumentException ->
                    handleIllegalArgumentException(illegalArgumentException, model, anyDTO, view);
            case IllegalAccessException illegalAccessException ->
                    handleIllegalAccessException(illegalAccessException, redirectAttributes);
            case DataIntegrityViolationException ignored ->
                    handleDataIntegrityViolationException(model, anyDTO, view);
            case null, default -> handleUnexpectedException(model, anyDTO, view);
        };
    }

    private String handleIllegalArgumentException(IllegalArgumentException ex, Model model, Object anyDTO, String view){
        return handleError(ex.getMessage(), model, anyDTO, view);
    }

    private String handleIllegalAccessException(IllegalAccessException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/login";
    }

    private String handleDataIntegrityViolationException(Model model, Object anyDTO, String view){
        return handleError("Erro de integridade de dados.", model, anyDTO, view);
    }

    private String handleUnexpectedException(Model model, Object anyDTO, String view){
        return handleError("Erro inesperado. Tente novamente.", model, anyDTO, view);

    }

    private String handleError(String errorMessage, Model model, Object anyDTO, String view){
        if (model != null) {
            model.addAttribute("errorMessage", errorMessage);
            if (anyDTO instanceof UserRegistrationDTO) {
                model.addAttribute("userRegistrationDTO", anyDTO);
            } else if (anyDTO instanceof UserRecoveryPasswordDTO) {
                model.addAttribute("userRecoveryPasswordDTO", anyDTO);
            }
        }
        if(view != null){
            assert model != null;
            return view;
        }else{
            return "redirect:/login";
        }
    }

}