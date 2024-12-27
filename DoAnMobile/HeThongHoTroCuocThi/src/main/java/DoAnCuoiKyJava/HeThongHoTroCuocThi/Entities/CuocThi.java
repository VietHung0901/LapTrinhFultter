package DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "CuocThi")
public class CuocThi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenCuocThi", length = 50, nullable = false)
    private String tenCuocThi;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ngayThi;

    private int soLuongThiSinh;

    private String diaDiemThi;

    private Long loaiTruongId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monThi_id", referencedColumnName = "id")
    @ToString.Exclude
    @JsonBackReference
    private MonThi monThi;

    @OneToMany(mappedBy = "cuocThi", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonBackReference
    private List<ChiTietNoiDung> chiTietNoiDungs = new ArrayList<>();

    @OneToMany(mappedBy = "cuocThi", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonBackReference
    private List<ChiTietQuyDinh> chiTietQuyDinhs = new ArrayList<>();

    @OneToMany(mappedBy = "cuocThi", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonBackReference
    private List<PhieuDangKy> phieuDangKIES = new ArrayList<>();

    private int trangThai;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) !=
                Hibernate.getClass(o)) return false;
        Truong truong = (Truong) o;
        return getId() != null && Objects.equals(getId(),
                truong.getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
