package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Request;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class PhieuDangKyList {
    private Long id;
    private String tenCuocThi;
    private String cccd;
    private String hoTen;
    private int Diem;
}
