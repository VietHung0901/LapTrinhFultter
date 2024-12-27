package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Controllers;


import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.Role;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Class.JWTTokenUtil;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        // Kiểm tra tên người dùng và mật khẩu
        User user = userService.findByUsername(loginRequest.getUsername());
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        // Tạo JWT token
        String token = jwtTokenUtil.generateToken(user);

        return ResponseEntity.ok().body("Bearer " + token);
    }

    // Api lấy role
    @GetMapping("/api/getRole")
    public ResponseEntity<?> getUserRole(@RequestParam String username) {
        // Tìm người dùng dựa trên tên người dùng
        User user = userService.findByUsername(username);

        // Lấy danh sách các vai trò của người dùng
        Set<Role> roles = user.getRoles();

        // Lấy vai trò đầu tiên trong tập hợp vai trò
        String roleName = roles.iterator().next().getName();

        // Trả về các vai trò dưới dạng JSON
        return ResponseEntity.ok().body(roleName);
    }
}
