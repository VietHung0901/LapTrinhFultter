package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Controllers.User;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.NoiDung;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Request.NoiDungRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.ChiTietNoiDungService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.CuocThiService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.NoiDungService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/USER/NoiDung")
public class FlutterNoiDungController {
    private final ChiTietNoiDungService CTNDService;
    private final CuocThiService cuocThiService;
    private final NoiDungService noiDungService;

    // API lấy chi tiết cuộc thi theo id
    @GetMapping("/list/{cuocThiId}")
    public ResponseEntity<?> getNoiDungById(@PathVariable Long cuocThiId, @RequestHeader(value = "Authorization") String authorizationHeader) {
        try {
            CuocThi cuocThi = cuocThiService.getCuocThiById(cuocThiId).orElseThrow(() -> new EntityNotFoundException("CuocThi not found with id"));
            List<NoiDung> list = CTNDService.getAllNoiDungByCuocThi(cuocThi);
            List<NoiDungRequest> listApi = new ArrayList<>();
            for(NoiDung noiDung : list){
                NoiDungRequest noiDungRequest = new NoiDungRequest();
                noiDungRequest = noiDungService.mapToNoiDungRequest(noiDung);
                listApi.add(noiDungRequest);
            }
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Lấy danh sách nội dung thành công",
                    "data", listApi
            ));
        } catch (Exception e) {
            // Xử lý lỗi và trả về phản hồi thất bại
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Không thể lấy danh sách nội dung",
                    "error", e.getMessage()
            ));
        }
    }
}
