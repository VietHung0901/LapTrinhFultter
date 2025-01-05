package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Controllers.User;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.NoiDung;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuDangKy;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Request.NoiDungRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Request.PhieuDangKyList;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.PhieuDangKyService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/USER/PhieuDangKy")
public class FlutterPhieuDangKyController {

    private final UserService userService;
    private final PhieuDangKyService phieuDangKyService;

    @GetMapping("/list")
    public ResponseEntity<?> getNoiDungById(@RequestHeader(value = "Authorization") String authorizationHeader, Principal principal) {
        try {
            User user = userService.findByUsername(principal.getName());
            List<PhieuDangKy> listPDK = phieuDangKyService.getPdkByUser(user);
            List<PhieuDangKyList> listApi = new ArrayList<>();
            for(PhieuDangKy pdk : listPDK){
                PhieuDangKyList pdklist = new PhieuDangKyList();
                pdklist = phieuDangKyService.mapToPhieuDangKyList(pdk);
                listApi.add(pdklist);
            }
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Lấy danh sách phiếu đăng ký theo user thành công",
                    "data", listApi
            ));
        } catch (Exception e) {
            // Xử lý lỗi và trả về phản hồi thất bại
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Không thể lấy danh sách phiếu đăng ký theo user thất bại",
                    "error", e.getMessage()
            ));
        }
    }
}
