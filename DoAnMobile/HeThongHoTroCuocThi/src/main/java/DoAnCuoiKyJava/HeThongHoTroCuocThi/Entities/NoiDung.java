package DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.web.multipart.MultipartFile;

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
@Table(name = "NoiDung")
public class NoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tenNoiDung", length = 50, nullable = false)
    private String tenNoiDung;

    private String moTaNoiDung;

    //Dùng để lưu file
    private String fileBaiTapUrl;

    private String imageUrl;

    @OneToMany(mappedBy = "noiDung", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ChiTietNoiDung> chiTietNoiDungs = new ArrayList<>();

    private int trangThai;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) !=
                Hibernate.getClass(o)) return false;
        NoiDung noiDung = (NoiDung) o;
        return getId() != null && Objects.equals(getId(),
                noiDung.getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
