package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.UserControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.QuyDinh;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Request.QuyDinhCreateRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.QuyDinhService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/User/QuyDinhs")
@RequiredArgsConstructor
public class QuyDinhController {
    private final QuyDinhService quyDinhService;

    @GetMapping("/detail/id/{id}")
    public String showQuyDinh(@PathVariable Long id, @NotNull Model model) {
        QuyDinh quyDinh = quyDinhService.getQuyDinhById(id).orElseThrow(() -> new EntityNotFoundException(""));
        model.addAttribute("quyDinh", quyDinh);
        return "User/QuyDinh/detail";
    }
}
