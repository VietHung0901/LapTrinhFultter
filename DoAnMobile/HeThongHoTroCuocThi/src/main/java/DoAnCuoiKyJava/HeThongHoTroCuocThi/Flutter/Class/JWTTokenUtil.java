package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Class;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JWTTokenUtil {

    private final String SECRET_KEY = "MySuperSecureSecretKeyWithAtLeast32Characters!";
    private final UserService userService;

    // Tạo JWT token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return createToken(claims, userDetails.getUsername());
    }

    public String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 giờ
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 15)) // 15 giây
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Giải mã JWT token và lấy claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }

    // Lấy tên người dùng từ token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Kiểm tra token có hết hạn hay không
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
