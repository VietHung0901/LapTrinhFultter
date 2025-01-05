package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Controllers.Admin;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuKetQua;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Request.PhieuKetQuaRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.CuocThiService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.PhieuDangKyService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.PhieuKetQuaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ADMIN/PhieuKetQua")
public class AdminFlutterPhieuKetQuaController {

    @Autowired
    private PhieuKetQuaService phieuKetQuaService;
    @Autowired
    private PhieuDangKyService phieuDangKyService;
    @Autowired
    private CuocThiService cuocThiService;

    @PutMapping("/kiem-tra-danh-sach")
    public ResponseEntity<?> kiemTraDanhSachPhieuKetQua(@RequestBody List<PhieuKetQuaRequest> phieuKetQuaList, @RequestParam Long cuocThiId) {
        try {
            List<Integer> list = phieuDangKyService.kiemTraThongTin(phieuKetQuaList, cuocThiId);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Kiểm tra thông tin thành công.",
                    "data", list
            ));
        } catch (Exception e) {
            // Xử lý lỗi và trả về phản hồi thất bại
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "kiểm tra thông tin thất bại.",
                    "error", e.getMessage()
            ));
        }
    }

    // API để nhận danh sách phiếu kết quả và lưu vào cơ sở dữ liệu
    @PostMapping("/nhap-danh-sach")
    public ResponseEntity<?> nhapDanhSachPhieuKetQua(@RequestBody List<PhieuKetQuaRequest> phieuKetQuaList) {
        try {
            // Lưu danh sách vào cơ sở dữ liệu
            phieuKetQuaService.luuDanhSachPhieuKetQua(phieuKetQuaList);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Nhập danh sách phiếu kết quả thành công."
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Nhập danh sách phiếu kết quả thất bại.",
                    "error", e.getMessage()
            ));
        }
    }

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
