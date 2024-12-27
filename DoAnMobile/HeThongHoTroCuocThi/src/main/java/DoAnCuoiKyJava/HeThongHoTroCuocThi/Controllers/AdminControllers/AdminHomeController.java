package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.AdminControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/Admin")
@RequiredArgsConstructor
public class AdminHomeController {
    private final UserService userService;
    private final CuocThiService cuocThiService;
    private final PhieuDangKyService phieuDangKyService;
    private final QuyDinhService quyDinhService;
    private final MonThiService monThiService;
    private final LoaiTruongService loaiTruongService;

    @GetMapping
    public String home(Model model) {
        long totalUsers = userService.getTotalUsers();
        long totalCompetitions = cuocThiService.getTotalCuocThis();
        long totalRegistrations = phieuDangKyService.getTotalPDKs();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalCompetitions", totalCompetitions);
        model.addAttribute("totalRegistrations", totalRegistrations);

        List<Object[]> userCountsByLoaiTruong = userService.getUserCountsByLoaiTruong();
        model.addAttribute("userCountsByLoaiTruong", userCountsByLoaiTruong);
        return "Admin/index";
    }
}
