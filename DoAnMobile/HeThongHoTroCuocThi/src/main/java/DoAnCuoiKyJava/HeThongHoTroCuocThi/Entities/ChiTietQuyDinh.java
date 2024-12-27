package DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ChiTietQuyDinh")
public class ChiTietQuyDinh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quyDinh_id", referencedColumnName = "id")
    @ToString.Exclude
    private QuyDinh quyDinh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuocThi_id", referencedColumnName = "id")
    @ToString.Exclude
    private CuocThi cuocThi;
}
