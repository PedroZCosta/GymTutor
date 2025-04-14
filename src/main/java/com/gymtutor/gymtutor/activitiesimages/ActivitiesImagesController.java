package com.gymtutor.gymtutor.activitiesimages;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


@Controller
@RequestMapping("/admin/activities/{activitiesId}/images")
public class ActivitiesImagesController {

    // pegando caminho absoluto (Config WebConfig e UploadProperties + Application.Properties)
    @Value("${upload.dir.activities}")
    private String uploadDirActivities;

    private final ActivitiesImagesService imagesService;

    public ActivitiesImagesController(ActivitiesImagesService imagesService) {
        this.imagesService = imagesService;
    }

    @GetMapping
    public String listImages(
            @PathVariable int activitiesId,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return handleRequest(redirectAttributes, model, null, null, activitiesId,()->{
            var imagesList = imagesService.findByActivityModelActivitiesId(activitiesId);
            model.addAttribute("imagesList", imagesList);
            model.addAttribute("activitiesId", activitiesId);
            model.addAttribute("body", "/admin/activities/images/list");
            return "/fragments/layout";
        });
    }

    @GetMapping("/new")
    public String newImageForm(
            Model model,
            @PathVariable int activitiesId
    ) {
        model.addAttribute("activitiesImagesModel", new ActivitiesImagesModel());
        model.addAttribute("activitiesId", activitiesId);
        model.addAttribute("body", "/admin/activities/images/new");
        return "/fragments/layout";
    }

    @PostMapping("/new")
    public String createImage(
            @PathVariable int activitiesId,
            @Valid @ModelAttribute ActivitiesImagesModel activitiesImagesModel,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam("imageFile") MultipartFile imageFile
    ){
        if(bindingResult.hasErrors()){
            return handleValidationErrors(model, "/admin/activities/images/new", activitiesImagesModel, bindingResult, activitiesId,null);
        }

        return  handleRequest(redirectAttributes, model, "admin/activities/images/new", activitiesImagesModel, activitiesId, ()-> {
            // Salvar a imagem
            String imagePath = saveImage(imageFile, activitiesImagesModel.getImageName().trim());
            System.out.println("Caminho da imagem: " + imagePath);
            activitiesImagesModel.setImagePath(imagePath);

            // Salvar a imagem no banco de dados
            imagesService.createImage(activitiesImagesModel, activitiesId);
            redirectAttributes.addFlashAttribute("successMessage","Imagem adicionada com sucesso!!!");
            return "redirect:/admin/activities/" + activitiesId + "/images";
        });
    }

    private String saveImage(MultipartFile imageFile, String imageName) {
        // Lógica para salvar a imagem no servidor e retornar o caminho
        try {

//            String directory = uploadProperties.getUploadDir();
            //utilizando caminho absoluto (Caminho relativo não está salvando a imagem)
            String directory = new File(uploadDirActivities).getAbsolutePath();

            // Cria a pasta se não existir,
            // metodo não funciona, o Security não permite criação e validação de diretórios, verificar permissões futuras
//            File dir = new File(directory);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }


            // pegando extensão do arquivo .jpg .bmp
            String extension = getFileExtension(Objects.requireNonNull(imageFile.getOriginalFilename()));
            // limpando sujeira do nome
            String newImageName = imageName.replaceAll("[^a-zA-Z0-9\\-_]", "_") + extension;

            // Garantindo unicidade no nome da imagem.
            File dest = new File(directory, newImageName);
            int count = 1;
            while (dest.exists()) {
                newImageName = imageName + "_" + count + extension;
                dest = new File(directory, newImageName);
                count++;
            }

            // Salva a imagem no destino
            imageFile.transferTo(dest);

            return "/images/activities/" + newImageName; // Retorna o caminho acessível pela URL pública (relativo ao static)
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar a imagem", e);
        }
    }

    // Metodo para obtenção da extensão da imagem
    private String getFileExtension(String fileName) {
        // Obtém a extensão do arquivo (ex: .jpg, .png, etc.)
        String extension = "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = fileName.substring(dotIndex);  // Retorna a extensão do arquivo
        }
        return extension;
    }

    @GetMapping("/{imageId}/edit")
    public String editImageForm(
            @PathVariable int activitiesId,
            @PathVariable int imageId,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return handleRequest(redirectAttributes, model, null, null, activitiesId, () -> {
            ActivitiesImagesModel activitiesImagesModel = imagesService.findById(imageId);
            model.addAttribute("activitiesImagesModel", activitiesImagesModel);
            model.addAttribute("activitiesId", activitiesId);
            model.addAttribute("body", "/admin/activities/images/edit");
            return "/fragments/layout";
        });
    }

    //TODO: Realizar deleção da imagem anterior após alterar, ou deixa lá consumindo espaço do servidor, alguém se vira com isso, já perdi 6 horas nessa parte
    //TODO: Como eu sou bonzinho o código de exclusão ta na linha 53 a 64 do service
    @PostMapping("/{imageId}/edit")
    public String updateImage(
            @PathVariable int activitiesId,
            @PathVariable int imageId,
            @Valid @ModelAttribute ActivitiesImagesModel activitiesImagesModel,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam("imageFile") MultipartFile imageFile
    ) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(model, "/admin/activities/images/edit", activitiesImagesModel, bindingResult, activitiesId, imageId);
        }

        return handleRequest(redirectAttributes, model, "admin/activities/images/edit", activitiesImagesModel, activitiesId, () -> {
            if (!imageFile.isEmpty()) {
                String newPath = saveImage(imageFile, activitiesImagesModel.getImageName().trim());
                activitiesImagesModel.setImagePath(newPath);
            }

            imagesService.updateImage(imageId, activitiesImagesModel);
            redirectAttributes.addFlashAttribute("successMessage", "Imagem atualizada com sucesso!");
            return "redirect:/admin/activities/" + activitiesId + "/images";
        });
    }


    @PostMapping("/delete/{imageId}")
    public String deleteImage(
            @PathVariable int activitiesId,
            @PathVariable int imageId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            imagesService.deleteImage(imageId);
            redirectAttributes.addFlashAttribute("successMessage", "Imagem excluída com sucesso!");
        } catch (DataIntegrityViolationException ex){
            redirectAttributes.addFlashAttribute("errorMessage","nao é possível excluir o video pois ha registros dependentes");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/activities/" + activitiesId + "/images";
    }

    private String handleValidationErrors(Model model, String view, ActivitiesImagesModel activitiesImagesModel, BindingResult bindingResult, Integer activitiesId, Integer imageId){
        model.addAttribute("errorMessage", "Há erros no formulário!");
        model.addAttribute("org.springframework.validation.BindingResult.activitiesImagesModel", bindingResult);
        model.addAttribute("ActivitiesImagesModel", activitiesImagesModel);
        model.addAttribute("activitiesId", activitiesId);

        if(imageId != null){
            model.addAttribute("imageId", imageId);
        }
        model.addAttribute("body", view);

        return "/fragments/layout";
    }

    private String handleRequest(RedirectAttributes redirectAttributes, Model model, String view, ActivitiesImagesModel activitiesImagesModel, Integer activitiesId, RequestHandler block
    ){
        try{
            return block.execute();
        }catch (Exception ex) {
            return handleException(ex, model, activitiesImagesModel, activitiesId, view, redirectAttributes);
        }
    }

    @FunctionalInterface
    public interface RequestHandler{
        String execute();
    }

    private String handleException(Exception ex, Model model, ActivitiesImagesModel activitiesImagesModel, Integer activitiesId, String view, RedirectAttributes redirectAttributes){
        return switch (ex) {
            case IllegalArgumentException illegalArgumentException ->
                    handleIllegalArgumentException(illegalArgumentException, model, activitiesImagesModel, activitiesId , view);
            case IllegalAccessException illegalAccessException ->
                    handleIllegalAccessException(illegalAccessException, redirectAttributes, activitiesId);
            case DataIntegrityViolationException ignored ->
                    handleDataIntegrityViolationException(model, activitiesImagesModel, activitiesId, view);
            case null, default -> handleUnexpectedException(model, activitiesImagesModel, activitiesId, view);
        };
    }

    private String handleIllegalArgumentException(IllegalArgumentException ex, Model model, ActivitiesImagesModel activitiesImagesModel,Integer activitiesId, String view){
        return handleError(ex.getMessage(), model, activitiesImagesModel, activitiesId, view);
    }

    private String handleIllegalAccessException(IllegalAccessException ex, RedirectAttributes redirectAttributes, Integer activitiesId){
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/admin/activities/"+ activitiesId +"/images";
    }

    private String handleDataIntegrityViolationException(Model model, ActivitiesImagesModel activitiesImagesModel, Integer activitiesId, String view){
        return handleError("Erro de integridade de dados.", model, activitiesImagesModel, activitiesId, view);
    }

    private String handleUnexpectedException(Model model, ActivitiesImagesModel activitiesImagesModel,Integer activitiesId, String view){
        return handleError("Erro inesperado. Tente novamente.", model, activitiesImagesModel, activitiesId, view);

    }

    private String handleError(String errorMessage, Model model, ActivitiesImagesModel activitiesImagesModel, Integer activitiesId, String view){
        if (model != null) {
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("activitiesImagesModel", activitiesImagesModel);
            model.addAttribute("activitiesId", activitiesId);
        }
        if(view != null){
            assert model != null;
            model.addAttribute("body", view);
            return "/fragments/layout";
        }else{
            return "redirect:/admin/activities/"+activitiesId+"/images";
        }
    }
}
