package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.ManagerControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuDangKy;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuSuaDiem;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.PhieuDangKyService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.PhieuSuaDiemService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/Manager/PhieuSuaDiems")
@RequiredArgsConstructor
public class ManagerPhieuSuaDiemController {
    private final PhieuSuaDiemService phieuSuaDiemService;
    private final PhieuDangKyService phieuDangKyService;
    private final UserService userService;

    @GetMapping
    public String showAllPhieuSuaDiemChuaDuyet(@NotNull Model model) {
        model.addAttribute("phieuSuaDiems", phieuSuaDiemService.findPhieuSuaDiemsByTrangThai(2));
        return "Manager/PhieuSuaDiem/list";
    }

    @GetMapping("/ThanhCong")
    public String showAllPhieuSuaDiemThanhCong(@NotNull Model model) {
        model.addAttribute("phieuSuaDiems", phieuSuaDiemService.findPhieuSuaDiemsByTrangThai(1));
        return "Manager/PhieuSuaDiem/list";
    }

    @GetMapping("/ThatBai")
    public String showAllPhieuSuaDiemThatBai(@NotNull Model model) {
        model.addAttribute("phieuSuaDiems", phieuSuaDiemService.findPhieuSuaDiemsByTrangThai(0));
        return "Manager/PhieuSuaDiem/list";
    }

    @GetMapping("/psd/id/{id}")
    public String GetPhieuSuaDiemByID(@NotNull Model model,
                                      @PathVariable Long id) {
        PhieuSuaDiem psd = phieuSuaDiemService.getPhieuSuaDiemById(id);
        model.addAttribute("phieuSuaDiem", psd);
        Long pdkID = psd.getPhieuKetQuaCu().getPhieuDangKy().getId();
        PhieuDangKy pdk = phieuDangKyService.getPhieuDangKyById(pdkID).orElseThrow(() -> new EntityNotFoundException(""));
        User user = userService.findById(pdk.getUser().getId());
        model.addAttribute("thongTinThiSinh", user);
        model.addAttribute("diemCu", psd.getPhieuKetQuaCu());
        model.addAttribute("diemMoi", psd.getPhieuKetQuaMoi());
        model.addAttribute("lyDoSua", psd.getLyDoSua());
        model.addAttribute("psdId", psd.getId());
        return "Manager/PhieuSuaDiem/details";
    }

    @GetMapping("/psd/trangthai/{trangthai}/{id}")
    public String DuyetPhieuSuaDiem(@PathVariable Long id,
                                    @PathVariable int trangthai) {
        phieuSuaDiemService.xuLyKetQuaSuaDiem(id, trangthai);
        if(trangthai == 1)
            return "redirect:/Manager/PhieuSuaDiems/ThanhCong";
        else
            return "redirect:/Manager/PhieuSuaDiems/ThatBai";
    }

    @GetMapping("/psd/duyetAll")
    public String DuyetAllPhieuSuaDiem() {
        phieuSuaDiemService.duyetAllPSD();
        return "redirect:/Manager/PhieuSuaDiems/ThanhCong";
    }
}
