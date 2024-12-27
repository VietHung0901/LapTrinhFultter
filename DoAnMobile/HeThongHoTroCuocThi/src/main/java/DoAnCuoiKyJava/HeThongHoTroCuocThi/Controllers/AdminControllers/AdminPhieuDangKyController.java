package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.AdminControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuDangKy;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/Admin/PhieuDangKys")
public class AdminPhieuDangKyController {
    private final PhieuDangKyService phieuDangKyService;
    private final CuocThiService cuocThiService;
    private final UserService userService;
    private final TruongService truongService;
    private final LoaiTruongService loaiTruongService;
    private final PhieuKetQuaService phieuKetQuaService;

    @GetMapping("/cuocThi/id/{id}")
    public String showAllPhieuDangKyTheoCuocThi(@NotNull Model model, @PathVariable Long id) {
        model.addAttribute("phieuDangKys", phieuDangKyService.getAllPhieuDangKystheoCuocThi(id));
        model.addAttribute("phieuKetQuaService", phieuKetQuaService);
        model.addAttribute("truongService", truongService);
        model.addAttribute("cuocThiId", id);
        return "Admin/PhieuDangKy/list";
    }

    @GetMapping("/edit/{id}")
    public String editPhieuDangKy(@PathVariable Long id, Model model) {
        PhieuDangKy phieuDangKy = phieuDangKyService.getPhieuDangKyById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tồn tại phiếu đăng ký có id: " + id));
        model.addAttribute("phieuDangKy", phieuDangKy);
        model.addAttribute("listTruong", truongService.getAllTruongsHien());
        model.addAttribute("loaiTruongService", loaiTruongService);
        return "Admin/PhieuDangKy/edit";
    }

    @PostMapping("/edit")
    public String updatePhieuDangKy(@Valid @ModelAttribute("PhieuDangKy") PhieuDangKy phieuDangKy,
                                    @NotNull BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "Admin/PhieuDangKy/edit";
        }
        phieuDangKyService.updatePhieuDangKy(phieuDangKy);
        return "redirect:/Admin/PhieuDangKys/cuocThi/id/" + phieuDangKy.getCuocThi().getId();
    }

    @GetMapping("/details/{id}")
    public String detailsPhieuDangKy(@PathVariable Long id, Model model) {
        PhieuDangKy phieuDangKy = phieuDangKyService.getPhieuDangKyById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tồn tại phiếu đăng ký có id: " + id));
        model.addAttribute("phieuDangKy", phieuDangKy);
        model.addAttribute("listTruong", truongService.getAllTruongsHien());
        model.addAttribute("loaiTruongService", loaiTruongService);
        model.addAttribute("truongService", truongService);
        return "Admin/PhieuDangKy/details";
    }
}