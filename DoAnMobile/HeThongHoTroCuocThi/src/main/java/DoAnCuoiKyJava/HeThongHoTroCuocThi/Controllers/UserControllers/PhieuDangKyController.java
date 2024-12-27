package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.UserControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.*;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Request.PhieuDangKyCreate;
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

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/User/PhieuDangKys")
public class PhieuDangKyController {
    private final PhieuDangKyService phieuDangKyService;
    private final CuocThiService cuocThiService;
    private final UserService userService;
    private final TruongService truongService;
    private final LoaiTruongService loaiTruongService;
    private final PhieuKetQuaService phieuKetQuaService;

    @GetMapping("/add/id/{id}")
    public String addPhieuDangKyForm(@NotNull Model model, @PathVariable Long id) {
        PhieuDangKyCreate phieuDangKy = new PhieuDangKyCreate();
        CuocThi cuocThi = cuocThiService.getCuocThiById(id)
                .orElseThrow(() -> new EntityNotFoundException("CuocThi not found with id: " + id));
        phieuDangKy.setCuocThi(cuocThi);
        model.addAttribute("phieuDangKy", phieuDangKy);
        model.addAttribute("cuocThi", cuocThi);
        model.addAttribute("loaiTruongService", loaiTruongService);
        return "User/PhieuDangKy/add";
    }

    @PostMapping("/add")
    public String addPhieuDangKy(
            @Valid @ModelAttribute("PhieuDangKy") PhieuDangKyCreate phieuDangKyCreate,
            @NotNull BindingResult bindingResult,
            Model model,
            Principal principal) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errorMessage", errors);

            CuocThi cuocThi = cuocThiService.getCuocThiById(phieuDangKyCreate.getCuocThi().getId())
                    .orElseThrow(() -> new EntityNotFoundException("CuocThi not found with id: "));
            phieuDangKyCreate.setCuocThi(cuocThi);
            model.addAttribute("phieuDangKy", phieuDangKyCreate);
            model.addAttribute("cuocThi", cuocThi);
            model.addAttribute("loaiTruongService", loaiTruongService);
            return "User/PhieuDangKy/add";
        }

        // Kiểm tra các dữ liệu đầu vào không được null
        if (phieuDangKyCreate.getUserId() == null
                || phieuDangKyCreate.getSdt() == null
                || phieuDangKyCreate.getEmail() == null
                || phieuDangKyCreate.getTruongId() == null) {
            model.addAttribute("errorMessage", "Vui lòng xác nhận thông tin");
            CuocThi cuocThi = cuocThiService.getCuocThiById(phieuDangKyCreate.getCuocThi().getId())
                    .orElseThrow(() -> new EntityNotFoundException("CuocThi not found with id: "));
            phieuDangKyCreate.setCuocThi(cuocThi);
            model.addAttribute("phieuDangKy", phieuDangKyCreate);
            model.addAttribute("cuocThi", cuocThi);
            model.addAttribute("loaiTruongService", loaiTruongService);
            return "User/PhieuDangKy/add";
        }

        // Kiểm tra trạng thái của thí sinh đã được duyệt chưa
        User user = userService.findById(phieuDangKyCreate.getUserId());
        if (!principal.getName().equals(user.getUsername()) ||  user.getTrangThai() != 1) {
            model.addAttribute("errorMessage", "Thông tin người đăng ký không phải của bạn hoặc thông tin của bạn chưa được xác thực.");
            CuocThi cuocThi = cuocThiService.getCuocThiById(phieuDangKyCreate.getCuocThi().getId())
                    .orElseThrow(() -> new EntityNotFoundException("CuocThi not found with id: "));
            phieuDangKyCreate.setCuocThi(cuocThi);
            model.addAttribute("phieuDangKy", phieuDangKyCreate);
            model.addAttribute("cuocThi", cuocThi);
            model.addAttribute("loaiTruongService", loaiTruongService);
            return "User/PhieuDangKy/add";
        }

        // Kiểm tra xem số lượng thí sinh đã đạt mức tối đa chưa
        int soLuongThiSinhHienTai = phieuDangKyService.countPhieuDangKyByCuocThiId(phieuDangKyCreate.getCuocThi().getId());
        if (soLuongThiSinhHienTai >= phieuDangKyCreate.getCuocThi().getSoLuongThiSinh()) {
            model.addAttribute("errorMessage", "Số lượng thí sinh tối đa cho cuộc thi này đã đạt.");
            CuocThi cuocThi = cuocThiService.getCuocThiById(phieuDangKyCreate.getCuocThi().getId())
                    .orElseThrow(() -> new EntityNotFoundException("CuocThi not found with id: "));
            phieuDangKyCreate.setCuocThi(cuocThi);
            model.addAttribute("phieuDangKy", phieuDangKyCreate);
            model.addAttribute("cuocThi", cuocThi);
            model.addAttribute("loaiTruongService", loaiTruongService);
            return "User/PhieuDangKy/add";
        }

        //Kiểm tra cấp học của người dùng có phù hợp với cuộc thi
//        User user = userService.findById(phieuDangKyCreate.getUserId());
        Truong truongUser = truongService.getTruongById(user.getTruong().getId())
                .orElseThrow(() -> new EntityNotFoundException(""));
        if (truongUser.getLoaiTruong().getId() != phieuDangKyCreate.getCuocThi().getLoaiTruongId()) {
            model.addAttribute("errorMessage", "Cuộc thi không dành cho cấp học của bạn");
            CuocThi cuocThi = cuocThiService.getCuocThiById(phieuDangKyCreate.getCuocThi().getId())
                    .orElseThrow(() -> new EntityNotFoundException("CuocThi not found with id: "));
            phieuDangKyCreate.setCuocThi(cuocThi);
            model.addAttribute("phieuDangKy", phieuDangKyCreate);
            model.addAttribute("cuocThi", cuocThi);
            model.addAttribute("loaiTruongService", loaiTruongService);
            return "User/PhieuDangKy/add";
        }

        // Kiểm tra xem người dùng đã đăng ký cuộc thi này chưa
        if (phieuDangKyService.tonTaiPhieuDangKyUserId_CuocThiId(phieuDangKyCreate.getUserId(), phieuDangKyCreate.getCuocThi().getId())) {
            model.addAttribute("errorMessage", "Bạn đã đăng ký cho cuộc thi này rồi.");
            CuocThi cuocThi = cuocThiService.getCuocThiById(phieuDangKyCreate.getCuocThi().getId())
                    .orElseThrow(() -> new EntityNotFoundException("CuocThi not found with id: "));
            phieuDangKyCreate.setCuocThi(cuocThi);
            model.addAttribute("phieuDangKy", phieuDangKyCreate);
            model.addAttribute("cuocThi", cuocThi);
            model.addAttribute("loaiTruongService", loaiTruongService);
            return "User/PhieuDangKy/add";
        }

        PhieuDangKy phieuDangKy = phieuDangKyService.mapToPhieuDangKy(phieuDangKyCreate);
        phieuDangKyService.addPhieuDangKy(phieuDangKy);
        return "redirect:/User/PhieuDangKys/search";
    }

    @GetMapping("/search")
    public String showSearchForm(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        List<PhieuDangKy> listPDK = phieuDangKyService.getPdkByUser(user);
        if(listPDK != null)
        {
            model.addAttribute("phieuDangKys", listPDK);
        }
        else{
            model.addAttribute("errorMessage", "Bạn chưa có phiếu đăng ký cuộc thi.");
        }
        return "User/PhieuDangKy/pdkSearch";
    }

    @GetMapping("/details/{id}")
    public String detailsPhieuDangKy(@PathVariable Long id, Model model) {
        PhieuDangKy phieuDangKy = phieuDangKyService.getPhieuDangKyById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tồn tại phiếu đăng ký có id: " + id));
        model.addAttribute("phieuDangKy", phieuDangKy);
        model.addAttribute("listTruong", truongService.getAllTruongsHien());
        model.addAttribute("loaiTruongService", loaiTruongService);
        model.addAttribute("truongService", truongService);
        return "User/PhieuDangKy/details";
    }
}