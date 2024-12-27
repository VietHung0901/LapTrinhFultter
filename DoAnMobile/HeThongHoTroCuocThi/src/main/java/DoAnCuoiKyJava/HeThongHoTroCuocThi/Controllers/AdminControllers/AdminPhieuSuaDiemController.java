package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.AdminControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.PhieuSuaDiemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/Admin/PhieuSuaDiems")
@RequiredArgsConstructor
public class AdminPhieuSuaDiemController {
    private final PhieuSuaDiemService phieuSuaDiemService;
}