package DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "PhieuSuaDiem")
public class PhieuSuaDiem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ngaySua;

    private String lyDoSua;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    @JsonBackReference
    private User nguoiSua;

    // Nhiều phiếu sửa điểm cho 1 phiếu kết quả
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phieuKetQuaCu_id", referencedColumnName = "id")
    @ToString.Exclude
    @JsonBackReference
    private PhieuKetQua phieuKetQuaCu; // Liên kết với phiếu kết quả cũ

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phieuKetQuaMoi_id", referencedColumnName = "id")
    @ToString.Exclude
    @JsonBackReference
    private PhieuKetQua phieuKetQuaMoi; // Liên kết với phiếu kết quả mới

    private int trangThai;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        PhieuSuaDiem phieuSuaDiem = (PhieuSuaDiem) o;
        return getId() != null && Objects.equals(getId(),
                phieuSuaDiem.getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
