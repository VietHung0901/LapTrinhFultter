package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.UserControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;
    private final CuocThiService cuocThiService;
    private final PhieuDangKyService phieuDangKyService;
    private final QuyDinhService quyDinhService;
    private final MonThiService monThiService;
    private final LoaiTruongService loaiTruongService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("quyDinhs", quyDinhService.getAllQuyDinhsHien());
        model.addAttribute("monThis", monThiService.getAllMonThis());
        model.addAttribute("loaiTruongs", loaiTruongService.getAllLoaiTruongs());
        return "User/index";
    }
}
