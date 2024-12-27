package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.AdminControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.MonThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.MonThiService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/Admin/MonThis")
@RequiredArgsConstructor
public class AdminMonThiController {
    private final MonThiService monThiService;

    @GetMapping
    public String showAllMonThi(@NotNull Model model) {
        model.addAttribute("monThis", monThiService.getAllMonThis());
        return "Admin/MonThi/list";
    }

    @GetMapping("/add")
    public String addMonThiForm(@NotNull Model model) {
        model.addAttribute("monThis", new MonThi());
        return "Admin/MonThi/add";
    }

    @PostMapping("/add")
    public String addMonThi(
            @Valid @ModelAttribute("") MonThi monThi,
            @NotNull BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "Admin/MonThi/add";
        }
        monThiService.addMonThi(monThi);
        return "redirect:/Admin/MonThis";
    }

    @GetMapping("/edit/{id}")
    public String editLoaiTruong(@PathVariable Long id, Model model) {
        MonThi monThi = monThiService.getMonThiById(id)
                .orElseThrow(() -> new EntityNotFoundException("MonThi not found with id: " + id));
        model.addAttribute("monThi", monThi);
        return "Admin/MonThi/edit";
    }

    @PostMapping("/edit")
    public String updateLoaiTruong(@Valid @ModelAttribute("MonThi") MonThi monthi,
                                   @NotNull BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "Admin/MonThi/edit";
        }
        monThiService.updateMonThi(monthi);
        return "redirect:/Admin/MonThis";
    }

    @GetMapping("/AnHien/{id}")
    public String toggleVisibility(@PathVariable Long id) {
        monThiService.AnHien(id);
        return "redirect:/Admin/MonThis";
    }
}
