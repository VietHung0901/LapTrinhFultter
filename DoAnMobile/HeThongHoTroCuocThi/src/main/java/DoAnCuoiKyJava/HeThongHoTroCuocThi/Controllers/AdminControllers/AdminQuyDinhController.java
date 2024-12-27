package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.AdminControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.QuyDinh;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Request.QuyDinhCreateRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.QuyDinhService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/Admin/QuyDinhs")
@RequiredArgsConstructor
public class AdminQuyDinhController {
    private final QuyDinhService quyDinhService;

    @GetMapping
    public String showAllQuyDinh(@NotNull Model model) {
        model.addAttribute("quyDinhs", quyDinhService.getAllQuyDinhs());
        return "Admin/QuyDinh/list";
    }

    @GetMapping("/id/{id}")
    public String showQuyDinh(@PathVariable Long id, @NotNull Model model) {
        QuyDinh quyDinh = quyDinhService.getQuyDinhById(id).orElseThrow(() -> new EntityNotFoundException(""));

        model.addAttribute("quyDinh", quyDinh);
        return "Admin/QuyDinh/detail";
    }

    @GetMapping("/add")
    public String addQuyDinhForm(@NotNull Model model) {
        model.addAttribute("quyDinh", new QuyDinhCreateRequest());
        return "Admin/QuyDinh/add";
    }

    @PostMapping("/add")
    public String addQuyDinh(
            @Valid @ModelAttribute("quyDinh") QuyDinhCreateRequest quyDinhCreateRequest,
            @NotNull BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            model.addAttribute("quyDinh", quyDinhCreateRequest);
            return "Admin/QuyDinh/add";
        }

        QuyDinh quyDinh = quyDinhService.mapToQuyDinh(quyDinhCreateRequest);
        quyDinhService.addQuyDinh(quyDinh);
        return "redirect:/Admin/QuyDinhs";
    }


    @GetMapping("/edit/{id}")
    public String editQuyDinh(@PathVariable Long id, Model model) {
        QuyDinh quyDinh = quyDinhService.getQuyDinhById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        model.addAttribute("quyDinh", quyDinh);
        return "Admin/QuyDinh/edit";
    }

    @PostMapping("/edit")
    public String updateQuyDinh(@Valid @ModelAttribute("QuyDinh") QuyDinh quyDinh,
                                @RequestParam(value = "imageUrlFile", required = false) MultipartFile imageUrlFile,
                                @NotNull BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errorMessage", errors);
            model.addAttribute("quyDinh", quyDinh);
            return "Admin/QuyDinh/edit";
        }

        String fileName = imageUrlFile.getOriginalFilename();
        if(fileName != "")
            quyDinh.setImageUrl(quyDinhService.saveImage(imageUrlFile));

        quyDinhService.updateQuyDinh(quyDinh);
        return "redirect:/Admin/QuyDinhs";
    }

    @GetMapping("/AnHien/{id}")
    public String toggleVisibility(@PathVariable Long id) {
        quyDinhService.AnHien(id);
        return "redirect:/Admin/QuyDinhs";
    }
}
