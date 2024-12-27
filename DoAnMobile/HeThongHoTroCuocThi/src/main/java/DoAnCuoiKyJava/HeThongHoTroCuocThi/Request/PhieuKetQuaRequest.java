package DoAnCuoiKyJava.HeThongHoTroCuocThi.Request;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuDangKy;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class PhieuKetQuaRequest {
    private Long id;
    private int phut;
    private int giay;
    private int diem;
    private PhieuDangKy phieuDangKy;
    private int hang;
}
