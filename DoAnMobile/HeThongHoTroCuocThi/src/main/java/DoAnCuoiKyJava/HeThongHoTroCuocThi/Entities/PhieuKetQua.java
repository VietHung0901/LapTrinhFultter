package DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "PhieuKetQua")
public class PhieuKetQua {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int phut;

    private int giay;

    private int diem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phieuDangKy_id", referencedColumnName = "id")
    @ToString.Exclude
    private PhieuDangKy phieuDangKy;

    private int trangThai;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) !=
                Hibernate.getClass(o)) return false;
        PhieuKetQua phieuKetQua = (PhieuKetQua) o;
        return getId() != null && Objects.equals(getId(),
                phieuKetQua.getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
