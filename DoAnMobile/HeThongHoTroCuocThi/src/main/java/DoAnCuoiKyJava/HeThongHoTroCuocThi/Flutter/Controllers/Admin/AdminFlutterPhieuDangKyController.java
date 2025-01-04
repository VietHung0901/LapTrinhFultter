package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Controllers.Admin;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuDangKy;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuKetQua;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Request.PhieuDangKyList;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.PhieuDangKyService;
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
@RequestMapping("/api/ADMIN/PhieuDangKy")
public class AdminFlutterPhieuDangKyController {
    private final PhieuDangKyService phieuDangKyService;

    @GetMapping("/list")
    public ResponseEntity<?> danhSachPhieuDangKyTheoCuocThi(@RequestParam Long cuocThiId) {
        try {
            List<PhieuDangKy> list = phieuDangKyService.getAllPhieuDangKystheoCuocThi(cuocThiId);
            List<PhieuDangKyList> listApi = new ArrayList<>();
            for(PhieuDangKy pdk : list){
                PhieuDangKyList pdkList = phieuDangKyService.mapToPhieuDangKyList(pdk);
                listApi.add(pdkList);
            }
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Lấy danh sách phiếu đăng ký thành công.",
                    "data", listApi
            ));
        } catch (Exception e) {
            // Xử lý lỗi và trả về phản hồi thất bại
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Lấy danh sách phiếu đăng ký thất bại.",
                    "error", e.getMessage()
            ));
        }
    }
}
