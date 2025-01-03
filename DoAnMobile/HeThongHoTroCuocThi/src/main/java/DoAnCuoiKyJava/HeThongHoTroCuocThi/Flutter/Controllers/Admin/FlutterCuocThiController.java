package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Controllers.Admin;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Class.JWTTokenUtil;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.CuocThiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ADMIN/CuocThi")
public class FlutterCuocThiController {

    @Autowired
    private CuocThiService cuocThiService;
    private final JWTTokenUtil jwtTokenUtil;

    // API để lấy danh sách các cuộc thi
    @GetMapping("/list")
    public ResponseEntity<?> getAllCuocThi(@RequestHeader(value = "Authorization") String authorizationHeader){
        List<CuocThi> cuocThis = cuocThiService.getAllCuocThis();
        return ResponseEntity.ok(cuocThis);
    }

    // API lấy chi tiết cuộc thi theo id
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getContestById(@PathVariable Long id, @RequestHeader(value = "Authorization") String authorizationHeader) {
        CuocThi contest = cuocThiService.getCuocThiById(id).orElseThrow();
        if (contest != null) {
            return ResponseEntity.ok(contest);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
