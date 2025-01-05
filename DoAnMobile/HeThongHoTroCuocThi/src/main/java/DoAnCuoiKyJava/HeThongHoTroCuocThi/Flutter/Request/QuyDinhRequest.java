package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Request;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class QuyDinhRequest {
    private Long id;
    private String tenQuyDinh;
    private String moTa;
}
