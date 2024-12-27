package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.AdminControllers;

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

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/Admin/CuocThis")
public class AdminCuocThiController {
    private final CuocThiService cuocThiService;
    private final MonThiService monThiService;
    private final NoiDungService noiDungService;
    private final QuyDinhService quyDinhService;
    private final ChiTietNoiDungService CTNDService;
    private final ChiTietQuyDinhService CTQDService;
    private final LoaiTruongService loaiTruongService;
    private final PhieuDangKyService phieuDangKyService;

    @GetMapping
    public String showAllCuocThi(@NotNull Model model) {
        model.addAttribute("cuocThis", cuocThiService.getAllCuocThis());
        model.addAttribute("loaiTruongService", loaiTruongService);
        model.addAttribute("phieuDangKyService", phieuDangKyService);
        model.addAttribute("monThis", monThiService.getAllMonThis());
        model.addAttribute("loaiTruongs", loaiTruongService.getAllLoaiTruongs());
        return "Admin/CuocThi/list";
    }

    @GetMapping("/add")
    public String addCuocThiForm(@NotNull Model model) {
        model.addAttribute("cuocThi", new CuocThi());
        model.addAttribute("listMonThi",monThiService.getAllMonThisHien());
        model.addAttribute("allQuyDinhs", quyDinhService.getAllQuyDinhsHien());
        model.addAttribute("allNoiDungs", noiDungService.getAllNoiDungsHien());
        model.addAttribute("listLoaiTruong",loaiTruongService.getAllLoaiTruongsHien());
        return "Admin/CuocThi/add";
    }

    @PostMapping("/add")
    public String addCuocThi(
            @Valid @ModelAttribute("CuocThi") CuocThi cuocThi,
            @RequestParam(value = "selectedNoiDungs", required = false) List<Long> selectedNoiDungIds,
            @RequestParam(value = "selectedQuyDinhs", required = false) List<Long> selectedQuyDinhIds,
            @NotNull BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errorMessage", errors);
            model.addAttribute("cuocThi", cuocThi);
            model.addAttribute("listMonThi",monThiService.getAllMonThisHien());
            model.addAttribute("allQuyDinhs", quyDinhService.getAllQuyDinhsHien());
            model.addAttribute("allNoiDungs", noiDungService.getAllNoiDungsHien());
            model.addAttribute("listLoaiTruong",loaiTruongService.getAllLoaiTruongsHien());
            return "Admin/CuocThi/add";
        }

        if (cuocThi.getTenCuocThi() == null
                || cuocThi.getNgayThi() == null || LocalDate.from(cuocThi.getNgayThi()).isBefore(LocalDate.now())
                || cuocThi.getSoLuongThiSinh() <= 0
                || cuocThi.getDiaDiemThi() == null
                || cuocThi.getMonThi() == null
                || cuocThi.getLoaiTruongId() == null) {
            model.addAttribute("errorMessage", "Thông tin cuộc thi không hợp lệ, vui lòng kiểm tra lại!");
            model.addAttribute("cuocThi", cuocThi);
            model.addAttribute("listMonThi", monThiService.getAllMonThisHien());
            model.addAttribute("allQuyDinhs", quyDinhService.getAllQuyDinhsHien());
            model.addAttribute("allNoiDungs", noiDungService.getAllNoiDungsHien());
            model.addAttribute("listLoaiTruong", loaiTruongService.getAllLoaiTruongsHien());
            return "Admin/CuocThi/add";
        }

        cuocThiService.addCuocThi(cuocThi);

        for (Long noiDungId : selectedNoiDungIds) {
            ChiTietNoiDung ctnd = new ChiTietNoiDung();
            NoiDung nd = noiDungService.getNoiDungById(noiDungId).orElseThrow(() -> new EntityNotFoundException("LoaiTruong not found with id: "));

            ctnd.setNoiDung(nd);
            ctnd.setCuocThi(cuocThi);
            CTNDService.addChiTietNoiDung(ctnd);
        }

        for (Long quyDinhId : selectedQuyDinhIds) {
            ChiTietQuyDinh ctqd = new ChiTietQuyDinh();
            QuyDinh qd = quyDinhService.getQuyDinhById(quyDinhId).orElseThrow(() -> new EntityNotFoundException("LoaiTruong not found with id: "));

            ctqd.setQuyDinh(qd);
            ctqd.setCuocThi(cuocThi);
            CTQDService.addChiTietQuyDinh(ctqd);
        }
        return "redirect:/Admin/CuocThis";
    }

    @GetMapping("/edit/{id}")
    public String editCuocThi(@PathVariable Long id, Model model) {
        CuocThi cuocThi = cuocThiService.getCuocThiById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        model.addAttribute("cuocThi", cuocThi);
        model.addAttribute("listMonThi", monThiService.getAllMonThisHien());
        model.addAttribute("allQuyDinhs", quyDinhService.getAllQuyDinhsHien());
        model.addAttribute("allNoiDungs", noiDungService.getAllNoiDungsHien());
        model.addAttribute("listLoaiTruong", loaiTruongService.getAllLoaiTruongsHien());

        List<NoiDung> chiTietNoiDungs = CTNDService.getAllNoiDungByCuocThi(cuocThi);
        List<QuyDinh> chiTietQuyDinhs = CTQDService.getAllQuyDinhByCuocThi(cuocThi);
        model.addAttribute("chiTietNoiDungs", chiTietNoiDungs);
        model.addAttribute("chiTietQuyDinhs", chiTietQuyDinhs);
        return "Admin/CuocThi/edit";
    }

    @PostMapping("/edit")
    public String updateCuocThi(@Valid @ModelAttribute("CuocThi") CuocThi cuocThi,
                               @NotNull BindingResult bindingResult,
                                @RequestParam(value = "selectedNoiDungs", required = false) List<Long> selectedNoiDungIds,
                                @RequestParam(value = "selectedQuyDinhs", required = false) List<Long> selectedQuyDinhIds,
                               Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            model.addAttribute("listMonThi",monThiService.getAllMonThisHien());
            return "Admin/CuocThi/edit";
        }

        if(selectedQuyDinhIds == null)
        {
            model.addAttribute("cuocThi", cuocThi);
            model.addAttribute("listMonThi", monThiService.getAllMonThisHien());
            model.addAttribute("allQuyDinhs", quyDinhService.getAllQuyDinhsHien());
            model.addAttribute("allNoiDungs", noiDungService.getAllNoiDungsHien());
            model.addAttribute("listLoaiTruong", loaiTruongService.getAllLoaiTruongsHien());
            List<NoiDung> chiTietNoiDungs = CTNDService.getAllNoiDungByCuocThi(cuocThi);
            List<QuyDinh> chiTietQuyDinhs = CTQDService.getAllQuyDinhByCuocThi(cuocThi);
            model.addAttribute("chiTietNoiDungs", chiTietNoiDungs);
            model.addAttribute("chiTietQuyDinhs", chiTietQuyDinhs);
            model.addAttribute("errorMessageQuyDinh", "Vui lòng chọn ít nhất 1 quy định!");
            return "Admin/CuocThi/edit";
        }
        if(selectedNoiDungIds == null)
        {
            model.addAttribute("cuocThi", cuocThi);
            model.addAttribute("listMonThi", monThiService.getAllMonThisHien());
            model.addAttribute("allQuyDinhs", quyDinhService.getAllQuyDinhsHien());
            model.addAttribute("allNoiDungs", noiDungService.getAllNoiDungsHien());
            model.addAttribute("listLoaiTruong", loaiTruongService.getAllLoaiTruongsHien());
            List<NoiDung> chiTietNoiDungs = CTNDService.getAllNoiDungByCuocThi(cuocThi);
            List<QuyDinh> chiTietQuyDinhs = CTQDService.getAllQuyDinhByCuocThi(cuocThi);
            model.addAttribute("chiTietNoiDungs", chiTietNoiDungs);
            model.addAttribute("chiTietQuyDinhs", chiTietQuyDinhs);
            model.addAttribute("errorMessageNoiDung", "Vui lòng chọn ít nhất 1 nội dung!");
            return "Admin/CuocThi/edit";
        }

        CTNDService.deleteAllNoiDungByCuocThi(cuocThi.getId());
        CTQDService.deleteAllQuyDinhByCuocThi(cuocThi.getId());

        //Thêm lại các nội dung và quy định của cuộc thi
        for (Long noiDungId : selectedNoiDungIds) {
            ChiTietNoiDung ctnd = new ChiTietNoiDung();
            NoiDung nd = noiDungService.getNoiDungById(noiDungId).orElseThrow(() -> new EntityNotFoundException("LoaiTruong not found with id: "));

            ctnd.setNoiDung(nd);
            ctnd.setCuocThi(cuocThi);
            CTNDService.addChiTietNoiDung(ctnd);
        }

        for (Long quyDinhId : selectedQuyDinhIds) {
            ChiTietQuyDinh ctqd = new ChiTietQuyDinh();
            QuyDinh qd = quyDinhService.getQuyDinhById(quyDinhId).orElseThrow(() -> new EntityNotFoundException("LoaiTruong not found with id: "));

            ctqd.setQuyDinh(qd);
            ctqd.setCuocThi(cuocThi);
            CTQDService.addChiTietQuyDinh(ctqd);
        }
        cuocThiService.updateCuocThi(cuocThi);
        return "redirect:/Admin/CuocThis";
    }

    @GetMapping("/AnHien/{id}")
    public String toggleVisibility(@PathVariable Long id) {
        cuocThiService.AnHien(id);
        return "redirect:/Admin/CuocThis";
    }

    @GetMapping("/details/{id}")
    public String detailsCuocThi(@PathVariable Long id, Model model) {
        CuocThi cuocThi = cuocThiService.getCuocThiById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        List<ChiTietNoiDung> chiTietNoiDungs = CTNDService.getChiTietNoiDungsByCuocThiId(id);
        List<ChiTietQuyDinh> chiTietQuyDinhs = CTQDService.getChiTietQuyDinhsByCuocThiId(id);

        model.addAttribute("cuocThi", cuocThi);
        model.addAttribute("chiTietNoiDungs", chiTietNoiDungs);
        model.addAttribute("chiTietQuyDinhs", chiTietQuyDinhs);
        model.addAttribute("loaiTruongService", loaiTruongService);
        return "Admin/CuocThi/details";
    }

    @GetMapping("/search")
    public String searchCuocThi(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(name = "location", required = false) String diaDiemThi,
            @RequestParam(name = "monThi", required = false) Long monThiId,
            @RequestParam(name = "capThi", required = false) Long loaiTruongId,
            Model model) {

        List<CuocThi> results = cuocThiService.getAllCuocThis();

        if (startDate == null && endDate == null && (diaDiemThi == null || diaDiemThi.isEmpty()) && monThiId == -1 && loaiTruongId == -1) {
            model.addAttribute("cuocThis", results);
            model.addAttribute("loaiTruongService", loaiTruongService);
            model.addAttribute("phieuDangKyService", phieuDangKyService);
            model.addAttribute("monThis", monThiService.getAllMonThis());
            model.addAttribute("loaiTruongs", loaiTruongService.getAllLoaiTruongs());
            return "Admin/CuocThi/list";
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
        model.addAttribute("monThis", monThiService.getAllMonThis());
        model.addAttribute("loaiTruongs", loaiTruongService.getAllLoaiTruongs());
        model.addAttribute("loaiTruongService", loaiTruongService);
        model.addAttribute("phieuDangKyService", phieuDangKyService);
        return "Admin/CuocThi/list";
    }

    @GetMapping("/quy-dinh/{id}")
    public String viewQuyDinh(@PathVariable Long id,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "1") int size,
                              Model model) {
        Pageable pageable = PageRequest.of(page, size); // Khởi tạo phân trang với mỗi trang có 1 quy định

        // Lấy danh sách các ChiTietQuyDinh với phân trang
        Page<ChiTietQuyDinh> chiTietQuyDinhsPage = CTQDService.getChiTietQuyDinhsByCuocThiId(id, pageable);

        // Thêm dữ liệu vào model để hiển thị trong view
        model.addAttribute("chiTietQuyDinhsPage", chiTietQuyDinhsPage);
        model.addAttribute("cuocThiId", id); // Truyền id của cuộc thi để dùng trong các liên kết phân trang
        return "Admin/CuocThi/quyDinh"; // Đường dẫn đến trang quy định
    }


}