package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Controllers.User;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.CuocThiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/USER/CuocThi")
public class FlutterCuocThiController {
    private final CuocThiService cuocThiService;

    @GetMapping("/list")
    public ResponseEntity<?> getAllCuocThi(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        try {
            List<CuocThi> cuocThis = cuocThiService.getAllCuocThis();
            // Trả về phản hồi với thông tin thành công và danh sách các cuộc thi
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Lấy danh sách cuộc thi thành công",
                    "data", cuocThis
            ));
        } catch (Exception e) {
            // Xử lý lỗi và trả về phản hồi thất bại
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Không thể lấy danh sách cuộc thi",
                    "error", e.getMessage()
            ));
        }
    }

    // API lấy chi tiết cuộc thi theo id
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getContestById(@PathVariable Long id, @RequestHeader(value = "Authorization") String authorizationHeader) {
        try {
            CuocThi contest = cuocThiService.getCuocThiById(id).orElseThrow();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Lấy cuộc thi thành công",
                    "data", contest
            ));
        } catch (Exception e) {
            // Xử lý lỗi và trả về phản hồi thất bại
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Không thể lấy danh sách cuộc thi",
                    "error", e.getMessage()
            ));
        }
    }

}
