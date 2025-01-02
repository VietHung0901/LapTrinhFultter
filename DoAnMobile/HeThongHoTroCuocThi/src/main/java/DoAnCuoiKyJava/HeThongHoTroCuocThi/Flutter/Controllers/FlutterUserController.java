package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Controllers;


import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.Role;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Class.JWTTokenUtil;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Class.LoginRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;


@RestController
public class FlutterUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Không dùng BCryptPasswordEncoder trực tiếp

    @Autowired
    private JWTTokenUtil jwtTokenUtil;

    // API đăng nhập
    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Kiểm tra tên người dùng
        User user = userService.findByUsername(loginRequest.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", "Invalid username or password"));
        }

        // Kiểm tra mật khẩu
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", "Invalid username or password"));
        }

        // Lấy danh sách các vai trò của người dùng
        Set<Role> roles = user.getRoles();

        // Lấy vai trò đầu tiên trong tập hợp vai trò
        String roleName = roles.iterator().next().getName();

        // Tạo JWT token
        String accesstoken = jwtTokenUtil.generateAccessToken(user);
        String refreshtoken = jwtTokenUtil.generateRefreshToken(user);

        // Trả về thông tin JSON chuẩn
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Login successful",
                "data", Map.of(
                        "accesstoken", "Bearer " + accesstoken,
                        "refreshtoken", refreshtoken,
                        "username", user.getUsername(),
                        "role", roleName
                )
        ));
    }

    // Endpoint để refresh token
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh token is missing");
        }

        try {
            // Giải mã refresh token
            String username = jwtTokenUtil.extractUsername(refreshToken);

            // Kiểm tra nếu refresh token hợp lệ
            if (jwtTokenUtil.isTokenExpired(refreshToken)) {
                return ResponseEntity.status(401).body("Refresh token is expired");
            }

            // Tìm người dùng trong cơ sở dữ liệu
            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(401).body("User not found");
            }

            // Tạo mới access token và refresh token
            String newAccessToken = jwtTokenUtil.generateAccessToken(user);
            String newRefreshToken = jwtTokenUtil.generateRefreshToken(user);

            // Trả về các token mới
            return ResponseEntity.ok(Map.of(
                    "accesstoken", newAccessToken,
                    "refreshtoken", newRefreshToken
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }
    }
}
