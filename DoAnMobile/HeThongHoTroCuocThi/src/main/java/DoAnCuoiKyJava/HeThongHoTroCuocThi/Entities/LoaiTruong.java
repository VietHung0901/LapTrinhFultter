package DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "loaitruong")
public class LoaiTruong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tenLoaiTruong", length = 50, nullable = false)
    private String tenLoaiTruong;

    @OneToMany(mappedBy = "loaiTruong", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Truong> truongs = new ArrayList<>();

    private int trangThai;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) !=
                Hibernate.getClass(o)) return false;
        LoaiTruong loaiTruong = (LoaiTruong) o;
        return getId() != null && Objects.equals(getId(),
                loaiTruong.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
