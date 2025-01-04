package DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Request;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class PhieuKetQuaShow {
    private Long id;
    private String tenCuocThi;
    private String cccd;
    private String hoten;
    private int phut;
    private int giay;
    private int diem;
}
