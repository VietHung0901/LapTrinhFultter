package DoAnCuoiKyJava.HeThongHoTroCuocThi.Request;


import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.MonThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.NoiDung;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.QuyDinh;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CuocThiCreateRequest {
    private Long Id;
    private String diaDiemThi;
    private LocalDateTime ngayThi;
    private int soLuong;
    private String tenCuocThi;
    private MonThi monThi;
}
