package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.AdminSchoolControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuKetQua;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.Truong;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/ADMIN_SCHOOL/PhieuKetQuas")
@RequiredArgsConstructor
public class AdminSchoolKetQuaController {

    private final PhieuDangKyService phieuDangKyService;
    private final CuocThiService cuocThiService;
    private final UserService userService;
    private final TruongService truongService;
    private final LoaiTruongService loaiTruongService;
    private final PhieuKetQuaService phieuKetQuaService;
    private final MonThiService monThiService;

    @GetMapping("/cuocThiId/{id}")
    public String showAllPhieuKetQuaByCuocThi(@PathVariable Long id, @NotNull Model model,  @RequestParam(value = "truongId", required = false) Long truongId) {
        CuocThi cuocThi = cuocThiService.getCuocThiById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy cuộc thi với ID: " + id));

        List<Truong> truongs = truongService.getAllTruongs();
        model.addAttribute("truongs", truongs);

        List<PhieuKetQua> phieuKetQuas;
        if (truongId != null && truongId > 0) {
            phieuKetQuas = phieuKetQuaService.getPhieuKetQuaByCuocThiAndTruong(cuocThi, truongId);
        } else {
            phieuKetQuas = phieuKetQuaService.getAllPhieuKetQuastheoCuocThi(cuocThi);
        }

        model.addAttribute("cuocThi", cuocThi);
        model.addAttribute("phieuKetQuas", phieuKetQuas);
        
        return "ADMIN_SCHOOL/PhieuKetQua/list";
    }

}