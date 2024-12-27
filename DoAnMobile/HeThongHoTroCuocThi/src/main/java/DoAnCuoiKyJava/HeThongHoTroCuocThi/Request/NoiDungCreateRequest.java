package DoAnCuoiKyJava.HeThongHoTroCuocThi.Request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class NoiDungCreateRequest {
    private Long id;
    private String tenNoiDung;
    private String moTaNoiDung;
    private MultipartFile imageUrl;
    private MultipartFile fileBaiTapUrl;
}
