package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Controllers.User;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.NoiDung;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.QuyDinh;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Request.NoiDungRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Request.QuyDinhRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.*;
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
@RequestMapping("/api/USER/QuyDinh")
public class FlutterQuyDinhController {
    private final ChiTietQuyDinhService CTQDService;
    private final CuocThiService cuocThiService;
    private final QuyDinhService quyDinhService;

    // API lấy chi tiết cuộc thi theo id
    @GetMapping("/list/{cuocThiId}")
    public ResponseEntity<?> getNoiDungById(@PathVariable Long cuocThiId, @RequestHeader(value = "Authorization") String authorizationHeader) {
        try {
            CuocThi cuocThi = cuocThiService.getCuocThiById(cuocThiId).orElseThrow(() -> new EntityNotFoundException("CuocThi not found with id"));
            List<QuyDinh> list = CTQDService.getAllQuyDinhByCuocThi(cuocThi);
            List<QuyDinhRequest> listApi = new ArrayList<>();
            for(QuyDinh quyDinh : list){
                QuyDinhRequest quyDinhRequest = new QuyDinhRequest();
                quyDinhRequest = quyDinhService.mapToQuyDinhRequest(quyDinh);
                listApi.add(quyDinhRequest);
            }
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Lấy danh sách quy định thành công",
                    "data", listApi
            ));
        } catch (Exception e) {
            // Xử lý lỗi và trả về phản hồi thất bại
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Không thể lấy danh sách quy định",
                    "error", e.getMessage()
            ));
        }
    }
}
