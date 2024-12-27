package DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "QuyDinh")
public class QuyDinh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tenQuyDinh", length = 50, nullable = false)
    private String tenQuyDinh;

    private String moTaQuyDinh;

    private String imageUrl;

    @OneToMany(mappedBy = "quyDinh", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ChiTietQuyDinh> chiTietQuyDinhs = new ArrayList<>();

    private int trangThai;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) !=
                Hibernate.getClass(o)) return false;
        QuyDinh quyDinh = (QuyDinh) o;
        return getId() != null && Objects.equals(getId(),
                quyDinh.getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
