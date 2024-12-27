package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.UserControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.*;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/User/CuocThis")
public class CuocThiController {
    private final CuocThiService cuocThiService;
    private final MonThiService monThiService;
    private final ChiTietNoiDungService CTNDService;
    private final ChiTietQuyDinhService CTQDService;
    private final LoaiTruongService loaiTruongService;
    private final PhieuDangKyService phieuDangKyService;
    private final UserService userService;
    private final PhieuNopBaiService phieuNopBaiService;

    @GetMapping
    public String showAllCuocThi(@NotNull Model model) {
        model.addAttribute("cuocThis", cuocThiService.getAllCuocThisHien());
        model.addAttribute("loaiTruongService", loaiTruongService);
        model.addAttribute("phieuDangKyService", phieuDangKyService);
        model.addAttribute("monThis", monThiService.getAllMonThisHien());
        model.addAttribute("loaiTruongs", loaiTruongService.getAllLoaiTruongsHien());
        return "User/CuocThi/list";
    }

    @GetMapping("/details/{id}")
    public String detailsCuocThi(@PathVariable Long id, Model model, Principal principal) {
        CuocThi cuocThi = cuocThiService.getCuocThiById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));

        List<ChiTietNoiDung> chiTietNoiDungs = CTNDService.getChiTietNoiDungsByCuocThiId(id);
        List<ChiTietQuyDinh> chiTietQuyDinhs = CTQDService.getChiTietQuyDinhsByCuocThiId(id);
        // Lấy thông tin người dùng từ Principal
        User user = userService.findByUsername(principal.getName());

        // Kiểm tra xem người dùng đã đăng ký cuộc thi này chưa
        boolean isRegistered = phieuDangKyService.tonTaiPhieuDangKyUserId_CuocThiId(user.getId(), cuocThi.getId());


        model.addAttribute("cuocThi", cuocThi);
        model.addAttribute("chiTietNoiDungs", isRegistered ? chiTietNoiDungs : Collections.emptyList()); // Chỉ hiển thị nếu đã đăng ký
        model.addAttribute("chiTietQuyDinhs", isRegistered ? chiTietQuyDinhs : Collections.emptyList()); // Chỉ hiển thị nếu đã đăng ký
        model.addAttribute("loaiTruongService", loaiTruongService);
        model.addAttribute("isRegistered", isRegistered);
        return "User/CuocThi/details";
    }

    @GetMapping("/search")
    public String searchCuocThiChoUser(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(name = "location", required = false) String diaDiemThi,
            @RequestParam(name = "monThi", required = false) Long monThiId,
            @RequestParam(name = "capThi", required = false) Long loaiTruongId,
            Model model) {

        List<CuocThi> results = cuocThiService.getAllCuocThisHien();

        if ((startDate == null || endDate == null) && diaDiemThi == null  && monThiId == -1 && loaiTruongId == -1) {
            model.addAttribute("cuocThis", results);
            model.addAttribute("loaiTruongService", loaiTruongService);
            model.addAttribute("phieuDangKyService", phieuDangKyService);
            model.addAttribute("monThis", monThiService.getAllMonThisHien());
            model.addAttribute("loaiTruongs", loaiTruongService.getAllLoaiTruongsHien());
            return "User/CuocThi/list";
        }

        if(startDate != null && endDate != null)
        {
            results = cuocThiService.searchByNgayThi(startDate, endDate, results);
        }

        if(diaDiemThi != null)
        {
            results = cuocThiService.searchByDiadime(diaDiemThi, results);
        }

        if(monThiId != -1)
        {
            results = cuocThiService.searchByMonThi(monThiId, results);
        }

        if(loaiTruongId != -1)
        {
            results = cuocThiService.searchByLoaiTruong(loaiTruongId, results);
        }

        model.addAttribute("cuocThis", results);
        model.addAttribute("monThis", monThiService.getAllMonThisHien());
        model.addAttribute("loaiTruongs", loaiTruongService.getAllLoaiTruongsHien());
        model.addAttribute("loaiTruongService", loaiTruongService);
        model.addAttribute("phieuDangKyService", phieuDangKyService);
        return "User/CuocThi/list";
    }

    @GetMapping("/quy-dinh/{id}")
    public String viewQuyDinh(@PathVariable Long id,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "1") int size,
                              Model model) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ChiTietQuyDinh> chiTietQuyDinhsPage = CTQDService.getChiTietQuyDinhsByCuocThiId(id, pageable);

        model.addAttribute("chiTietQuyDinhsPage", chiTietQuyDinhsPage);
        model.addAttribute("cuocThiId", id);
        return "/User/CuocThi/quyDinh";
    }

    @GetMapping("/noi-dung/{id}")
    public String viewNoiDung(@PathVariable Long id,
                              Model model,
                              Principal principal) {
        List<ChiTietNoiDung> chiTietNoiDungs = CTNDService.getChiTietNoiDungsByCuocThiId(id);
        model.addAttribute("chiTietNoiDungs", chiTietNoiDungs);
        model.addAttribute("cuocThiId", id);

        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        model.addAttribute("phieuNopBaiService", phieuNopBaiService);
        return "/User/NoiDung/list"; // Đường dẫn tới trang nội dung
    }
}