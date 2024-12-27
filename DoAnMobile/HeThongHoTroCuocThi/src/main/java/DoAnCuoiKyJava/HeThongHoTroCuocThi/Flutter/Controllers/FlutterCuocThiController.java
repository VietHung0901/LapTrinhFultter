package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Controllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.CuocThiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FlutterCuocThiController {

    @Autowired
    private CuocThiService cuocThiService;

    // API để lấy danh sách các cuộc thi
    @GetMapping("/api/cuocThi")
    public ResponseEntity<List<CuocThi>> getAllCuocThi() {
        List<CuocThi> cuocThis = cuocThiService.getAllCuocThis();
        return ResponseEntity.ok(cuocThis);
    }

    // API lấy chi tiết cuộc thi theo id
    @GetMapping("/api/cuocThi/{id}")
    public ResponseEntity<CuocThi> getContestById(@PathVariable Long id) {
        CuocThi contest = cuocThiService.getCuocThiById(id).orElseThrow();
        if (contest != null) {
            return ResponseEntity.ok(contest);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
