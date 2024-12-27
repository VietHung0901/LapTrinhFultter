package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.AdminSchoolControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ADMIN_SCHOOL")
@RequiredArgsConstructor
public class AdminSchoolHomeController {

    @GetMapping
    public String showUploadForm() {
        return "ADMIN_SCHOOL/index";
    }
}
