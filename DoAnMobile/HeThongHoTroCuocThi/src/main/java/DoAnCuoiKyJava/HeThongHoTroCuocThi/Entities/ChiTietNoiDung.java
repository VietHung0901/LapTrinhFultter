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
@Table(name = "ChiTietNoiDung")
public class ChiTietNoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noiDung_id", referencedColumnName = "id")
    @ToString.Exclude
    private NoiDung noiDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuocThi_id", referencedColumnName = "id")
    @ToString.Exclude
    private CuocThi cuocThi;
}
