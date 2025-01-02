package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Class;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.Role;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class JWTTokenUtil {

    private final String SECRET_KEY = "MySuperSecureSecretKeyWithAtLeast32Characters!";
    private final String REFRESH_SECRET_KEY = "MySuperSecureRefreshSecretKeyWithAtLeast32Characters!";
    private final UserService userService;

    // Tạo refresh token
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // Refresh token hết hạn sau 7 ngày
                .signWith(SignatureAlgorithm.HS256, REFRESH_SECRET_KEY)
                .compact();
    }

    // Tạo JWT token
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Token hết hạn sau 10 giờ
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 30)) // Token hết hạn sau 30 giây
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Giải mã JWT token
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Kiểm tra token có hợp lệ hay không
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Lấy ngày hết hạn của token
    private Date extractExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    // Hàm xác thực token
    public ResponseEntity<?> validateToken(String authorizationHeader, String rolename) {
        // Kiểm tra nếu header Authorization có chứa Bearer token
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", "Token is missing or invalid"));
        }

        // Lấy token từ header
        String token = authorizationHeader.substring(7); // Loại bỏ phần "Bearer " ra khỏi token

        try {
            // Kiểm tra xem token có hợp lệ hay không
            if (isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("status", "error", "message", "Token is expired"));
            }

            // Lấy thông tin người dùng từ token (ví dụ: username)
            String username = extractUsername(token);

            // Kiểm tra xem người dùng có hợp lệ không
            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("status", "error", "message", "Invalid user"));
            }

            // Lấy danh sách các vai trò của người dùng
            Set<Role> roles = user.getRoles();
            // Lấy vai trò đầu tiên trong tập hợp vai trò
            String roleName = roles.iterator().next().getName();
            if (!Objects.equals(roleName, rolename)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("status", "error", "message", "Access denied"));
            }

            // Trả về người dùng hợp lệ nếu tất cả các bước trên đều thành công
            return null; // Trả về null nếu token hợp lệ, sẽ xử lý tiếp ở controller

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", "Invalid or expired token"));
        }
    }
}
