package DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "MonThi")
public class MonThi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tenMonThi", length = 50, nullable = false)
    private String tenMonThi;

    @OneToMany(mappedBy = "monThi", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<CuocThi> cuocThis = new ArrayList<>();

    private int trangThai;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) !=
                Hibernate.getClass(o)) return false;
        MonThi monThi = (MonThi) o;
        return getId() != null && Objects.equals(getId(),
                monThi.getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
