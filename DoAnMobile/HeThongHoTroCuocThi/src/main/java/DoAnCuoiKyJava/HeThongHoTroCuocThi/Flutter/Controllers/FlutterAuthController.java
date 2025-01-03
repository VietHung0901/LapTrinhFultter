package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Controllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.Role;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Class.JWTTokenUtil;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Request.LoginRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class FlutterAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Không dùng BCryptPasswordEncoder trực tiếp

    @Autowired
    private JWTTokenUtil jwtTokenUtil;

    // API đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Kiểm tra tên người dùng
        User user = userService.findByUsername(loginRequest.getUsername());
        if (user == null) {
            return createErrorResponse("Invalid username or password");
        }

        // Kiểm tra mật khẩu
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
           return createErrorResponse("Invalid username or password");
        }

        // Lấy danh sách các vai trò của người dùng
        Set<Role> roles = user.getRoles();

        // Lấy vai trò đầu tiên trong tập hợp vai trò
        String roleName = roles.iterator().next().getName();

        // Tạo JWT token
        String token = jwtTokenUtil.generateToken(user);

        // Trả về thông tin JSON chuẩn
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Login successful",
                "data", Map.of(
                        "token", "Bearer " + token,
                        "username", user.getUsername(),
                        "role", roleName
                )
        ));
    }

    // Tạo phản hồi lỗi
    private ResponseEntity<?> createErrorResponse(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("status", "error", "message", message));
    }
}
