package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Controllers;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Class.PhieuKetQuaRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.PhieuDangKyService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.PhieuKetQuaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping()
public class FlutterPhieuKetQuaController {

    @Autowired
    private PhieuKetQuaService phieuKetQuaService;
    @Autowired
    private PhieuDangKyService phieuDangKyService;

    @PutMapping("/api/phieu-ket-qua/kiem-tra-danh-sach")
    public ResponseEntity<List<Integer>> kiemTraDanhSachPhieuKetQua(@RequestBody List<PhieuKetQuaRequest> phieuKetQuaList, @RequestParam Long cuocThiId) {
        try {
            // Kiểm tra và lấy danh sách kết quả kiểm tra
            List<Integer> list = phieuDangKyService.kiemTraThongTin(phieuKetQuaList, cuocThiId);

            // Trả về danh sách kết quả kiểm tra
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            // Trả về lỗi nếu có vấn đề trong quá trình xử lý
            return ResponseEntity.status(500).body(Collections.singletonList(-1));  // Gửi về giá trị -1 khi có lỗi
        }
    }


    // API để nhận danh sách phiếu kết quả và lưu vào cơ sở dữ liệu
    @PostMapping("/api/phieu-ket-qua/nhap-danh-sach")
    public ResponseEntity<String> nhapDanhSachPhieuKetQua(@RequestBody List<PhieuKetQuaRequest> phieuKetQuaList) {
        try {
            // Lưu danh sách vào cơ sở dữ liệu
            phieuKetQuaService.luuDanhSachPhieuKetQua(phieuKetQuaList);
            return ResponseEntity.ok("Nhập danh sách phiếu kết quả thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi nhập danh sách phiếu kết quả: " + e.getMessage());
        }
    }
}
