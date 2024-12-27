package DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "PhieuDangKy")
public class PhieuDangKy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ngayDangKy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuocThi_id", referencedColumnName = "id")
    @ToString.Exclude
    private CuocThi cuocThi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

    private String sdt;

    private String email;

    private Long truongId;

    private int trangThai;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) !=
                Hibernate.getClass(o)) return false;
        PhieuDangKy phieudangky = (PhieuDangKy) o;
        return getId() != null && Objects.equals(getId(),
                phieudangky.getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
