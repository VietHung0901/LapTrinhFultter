package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.UserControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.NoiDung;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Request.NoiDungCreateRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.NoiDungService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/NoiDungs")
@RequiredArgsConstructor
public class NoiDungController {
    private final NoiDungService noiDungService;

    @GetMapping
    public String showAllNoiDung(@NotNull Model model) {
        model.addAttribute("noiDungs", noiDungService.getAllNoiDungs());
        return "NoiDung/list";
    }

    @GetMapping("/id/{id}")
    public String showNoiDung(@PathVariable Long id, @NotNull Model model) {
        NoiDung noiDung = noiDungService.getNoiDungById(id).orElseThrow(() -> new EntityNotFoundException(""));
        model.addAttribute("noiDung", noiDung);
        return "NoiDung/detail";
    }

    @GetMapping("/add")
    public String addNoiDungForm(@NotNull Model model) {
        model.addAttribute("noiDung", new NoiDungCreateRequest());
        return "NoiDung/add";
    }

    @PostMapping("/add")
    public String addNoiDung(
            @Valid @ModelAttribute("NoiDung") NoiDungCreateRequest noiDungCreateRequest,
            @NotNull BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            model.addAttribute("noiDung", noiDungCreateRequest);
            return "NoiDung/add";
        }
        NoiDung noiDung = noiDungService.mapToNoiDung(noiDungCreateRequest);
        noiDungService.addNoiDung(noiDung);
        return "redirect:/NoiDungs";
    }


    @GetMapping("/edit/{id}")
    public String editNoiDung(@PathVariable Long id, Model model) {
        NoiDung noiDung = noiDungService.getNoiDungById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        model.addAttribute("noiDung", noiDung);
        return "NoiDung/edit";
    }

    @PostMapping("/edit")
    public String updateNoiDung(@Valid @ModelAttribute("NoiDung") NoiDung noiDung,
                                @RequestParam(value = "imageUrlFile", required = false) MultipartFile imageUrlFile,
                                @NotNull BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errorMessage", errors);
            model.addAttribute("noiDung", noiDung);
            return "NoiDung/edit";
        }

        String fileName = imageUrlFile.getOriginalFilename();
        if(fileName != "")
            noiDung.setImageUrl(noiDungService.saveImage(imageUrlFile));

        noiDungService.updateNoiDung(noiDung);
        return "redirect:/NoiDungs";
    }

    @GetMapping("/AnHien/{id}")
    public String toggleVisibility(@PathVariable Long id) {
        noiDungService.AnHien(id);
        return "redirect:/NoiDungs";
    }
}
