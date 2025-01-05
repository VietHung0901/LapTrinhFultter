package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Controllers.User;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuKetQua;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Request.PhieuKetQuaRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.CuocThiService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.PhieuKetQuaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/USER/PhieuKetQua")
public class FlutterPhieuKetQuaController {
    private final CuocThiService cuocThiService;
    private final PhieuKetQuaService phieuKetQuaService;

    @GetMapping("/xem-bang-ket-qua")
    public ResponseEntity<?> xemBangKetQuaTheoCuocThi(@RequestParam Long cuocThiId) {
        try {
            CuocThi cuocThi = cuocThiService.getCuocThiById(cuocThiId).orElse(null);
            List<PhieuKetQua> list = phieuKetQuaService.getAllPhieuKetQuastheoCuocThi(cuocThi);
            List<PhieuKetQuaRequest> listApi = new ArrayList<>();
            for(PhieuKetQua pkq : list) {
                PhieuKetQuaRequest request = phieuKetQuaService.mapToPhieuKetQuaRequestApi(pkq);
                listApi.add(request);
            }
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Lấy bảng kết quả thành công.",
                    "data", listApi
            ));
        } catch (Exception e) {
            // Xử lý lỗi và trả về phản hồi thất bại
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Lấy bảng kết quả thất bại.",
                    "error", e.getMessage()
            ));
        }
    }

}
