package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Controllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Class.JWTTokenUtil;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.CuocThiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FlutterCuocThiController {

    @Autowired
    private CuocThiService cuocThiService;
    private final JWTTokenUtil jwtTokenUtil;

    // API để lấy danh sách các cuộc thi
    @GetMapping("/api/cuocThi")
    public ResponseEntity<?> getAllCuocThi(@RequestHeader(value = "Authorization") String authorizationHeader){

        // Gọi hàm xác thực token
        ResponseEntity<?> tokenValidationResponse = jwtTokenUtil.validateToken(authorizationHeader, "MANAGER");
        if (tokenValidationResponse != null) {
            return tokenValidationResponse;  // Trả về nếu token không hợp lệ
        }

        List<CuocThi> cuocThis = cuocThiService.getAllCuocThis();
        return ResponseEntity.ok(cuocThis);
    }

    // API lấy chi tiết cuộc thi theo id
    @GetMapping("/api/cuocThi/{id}")
    public ResponseEntity<?> getContestById(@PathVariable Long id, @RequestHeader(value = "Authorization") String authorizationHeader) {

        // Gọi hàm xác thực token
        ResponseEntity<?> tokenValidationResponse = jwtTokenUtil.validateToken(authorizationHeader, "MANAGER");
        if (tokenValidationResponse != null) {
            return tokenValidationResponse;  // Trả về nếu token không hợp lệ
        }

        CuocThi contest = cuocThiService.getCuocThiById(id).orElseThrow();
        if (contest != null) {
            return ResponseEntity.ok(contest);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
