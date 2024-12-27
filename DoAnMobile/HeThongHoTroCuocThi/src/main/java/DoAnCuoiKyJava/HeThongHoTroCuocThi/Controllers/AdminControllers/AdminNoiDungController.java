package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.AdminControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.NoiDung;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Request.NoiDungCreateRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.NoiDungService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/Admin/NoiDungs")
@RequiredArgsConstructor
public class AdminNoiDungController {
    private final NoiDungService noiDungService;

    @GetMapping
    public String showAllNoiDung(@NotNull Model model) {
        model.addAttribute("noiDungs", noiDungService.getAllNoiDungs());
        return "Admin/NoiDung/list";
    }

    @GetMapping("/id/{id}")
    public String showNoiDung(@PathVariable Long id, @NotNull Model model) {
        NoiDung noiDung = noiDungService.getNoiDungById(id).orElseThrow(() -> new EntityNotFoundException(""));
        model.addAttribute("noiDung", noiDung);
        return "Admin/NoiDung/detail";
    }

    @GetMapping("/add")
    public String addNoiDungForm(@NotNull Model model) {
        model.addAttribute("noiDung", new NoiDungCreateRequest());
        return "Admin/NoiDung/add";
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
            return "Admin/NoiDung/add";
        }
        NoiDung noiDung = noiDungService.mapToNoiDung(noiDungCreateRequest);
        noiDungService.addNoiDung(noiDung);
        return "redirect:/Admin/NoiDungs";
    }


    @GetMapping("/edit/{id}")
    public String editNoiDung(@PathVariable Long id, Model model) {
        NoiDung noiDung = noiDungService.getNoiDungById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        model.addAttribute("noiDung", noiDung);
        return "Admin/NoiDung/edit";
    }

    @PostMapping("/edit")
    public String updateNoiDung(@Valid @ModelAttribute("NoiDung") NoiDung noiDung,
                                @RequestParam(value = "imageUrlFile", required = false) MultipartFile imageUrlFile,
                                @RequestParam(value = "fileBaiTap", required = false) MultipartFile fileBaiTap,
                                @NotNull BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errorMessage", errors);
            model.addAttribute("noiDung", noiDung);
            return "Admin/NoiDung/edit";
        }

        String fileName = imageUrlFile.getOriginalFilename();
        if(fileName != "")
            noiDung.setImageUrl(noiDungService.saveImage(imageUrlFile));

        String fileURLName = fileBaiTap.getOriginalFilename();
        if (!fileURLName.equals("")) {
            noiDung.setFileBaiTapUrl(noiDungService.saveFile(fileBaiTap));  // Thay đổi phương thức saveFile
        }

        noiDungService.updateNoiDung(noiDung);
        return "redirect:/Admin/NoiDungs";
    }

    @GetMapping("/AnHien/{id}")
    public String toggleVisibility(@PathVariable Long id) {
        noiDungService.AnHien(id);
        return "redirect:/Admin/NoiDungs";
    }
}
