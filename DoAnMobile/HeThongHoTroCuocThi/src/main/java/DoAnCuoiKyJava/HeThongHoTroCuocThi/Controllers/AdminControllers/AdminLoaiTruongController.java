package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.AdminControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.LoaiTruong;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.LoaiTruongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.ui.Model;
@Controller
@RequestMapping("/Admin/LoaiTruongs")
@RequiredArgsConstructor
public class AdminLoaiTruongController {
    private final LoaiTruongService loaiTruongService;

    @GetMapping
    public String showAllLoaiTruong(@NotNull Model model) {
        model.addAttribute("loaiTruongs", loaiTruongService.getAllLoaiTruongs());
        return "Admin/LoaiTruong/list";
    }

    @GetMapping("/add")
    public String addLoaiTruongForm(@NotNull Model model) {
        model.addAttribute("loaiTruong", new LoaiTruong());
        return "Admin/LoaiTruong/add";
    }

    @PostMapping("/add")
    public String addLoaiTruong(
            @Valid @ModelAttribute("loaitruong") LoaiTruong loaiTruong,
            @NotNull BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "LoaiTruong/add";
        }
        loaiTruongService.addLoaiTruong(loaiTruong);
        return "redirect:/Admin/LoaiTruongs";
    }

    @GetMapping("/edit/{id}")
    public String editLoaiTruong(@PathVariable Long id, Model model) {
        LoaiTruong loaiTruong = loaiTruongService.getLoaiTruongById(id);
        model.addAttribute("loaiTruong", loaiTruong);
        return "Admin/LoaiTruong/edit";
    }

    @PostMapping("/edit")
    public String updateLoaiTruong(@Valid @ModelAttribute("loaitruong") LoaiTruong loaitruong,
                                   @NotNull BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "Admin/LoaiTruong/edit";
        }
        loaiTruongService.updateLoaiTruong(loaitruong);
        return "redirect:/Admin/LoaiTruongs";
    }

    @GetMapping("/AnHien/{id}")
    public String toggleVisibility(@PathVariable Long id) {
        loaiTruongService.AnHien(id);
        return "redirect:/Admin/LoaiTruongs";
    }
}
