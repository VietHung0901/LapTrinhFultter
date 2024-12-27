package DoAnCuoiKyJava.HeThongHoTroCuocThi.Request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class QuyDinhCreateRequest {
    private Long id;
    private String tenQuyDinh;
    private String moTaQuyDinh;
    private MultipartFile imageUrl;
}
