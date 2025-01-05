package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Request;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class NoiDungRequest {
    private Long id;
    private String tenNoiDung;
    private String moTa;
}
