package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.AdminControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.*;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.PhieuNopBaiService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/Admin/PhieuNopBais")
@RequiredArgsConstructor
public class AdminPhieuNopBaiController {

    private final PhieuNopBaiService phieuNopBaiService;

    @GetMapping("/ChuaCham")
    public String showAllPhieuNopBaiChuaCham(@NotNull Model model) {
        model.addAttribute("phieuNopBai", phieuNopBaiService.findAllByTrangThai(0));
        return "Admin/PhieuNopBai/list";
    }

    @GetMapping("/DaCham")
    public String showAllPhieuNopBaiDaCham(@NotNull Model model) {
        model.addAttribute("phieuNopBai", phieuNopBaiService.findAllByTrangThai(1));
        return "/Admin/PhieuNopBai/list";
    }

    @GetMapping("/edit/{id}")
    public String showNoiDung(@PathVariable Long id, @NotNull Model model) {
        PhieuNopBai phieuNopBai = phieuNopBaiService.findById(id).orElseThrow();
        model.addAttribute("phieuNopBai", phieuNopBai);
        return "Admin/PhieuNopBai/edit";
    }

    @PostMapping("/edit")
    public String editPhieuNopBai(@NotNull Model model,
                                  @Valid @ModelAttribute("PhieuNopBai") PhieuNopBai phieuNopBai) {
        phieuNopBaiService.edit(phieuNopBai);
        return "redirect:/Admin/PhieuNopBais/ChuaCham";
    }

//
//    @GetMapping("/psd/trangthai/{trangthai}/{id}")
//    public String DuyetPhieuSuaDiem(@PathVariable Long id,
//                                    @PathVariable int trangthai) {
//        phieuSuaDiemService.xuLyKetQuaSuaDiem(id, trangthai);
//        if(trangthai == 1)
//            return "redirect:/Manager/PhieuSuaDiems/ThanhCong";
//        else
//            return "redirect:/Manager/PhieuSuaDiems/ThatBai";
//    }

    @GetMapping("/preview/{id}")
    public ResponseEntity<Resource> previewFile(@PathVariable Long id) {
        try {
            System.out.println("Preview file ID: " + id);
            PhieuNopBai phieuNopBai = phieuNopBaiService.findById(id).orElseThrow();
            Path filePath = Paths.get("uploads").resolve(phieuNopBai.getFileUrl()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new RuntimeException("File not found");
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + phieuNopBai.getFileUrl() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error resolving file URL", e);
        }
    }

}
