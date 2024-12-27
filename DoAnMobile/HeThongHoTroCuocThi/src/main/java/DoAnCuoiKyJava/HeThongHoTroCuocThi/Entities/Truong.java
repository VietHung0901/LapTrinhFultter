package DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Entity
@Table(name = "truong")
public class Truong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenTruong", length = 50, nullable = false)
    private String tenTruong;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "loaitruong_id", referencedColumnName = "id")
    @ToString.Exclude
    private LoaiTruong loaiTruong;

    @OneToMany(mappedBy = "truong", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<User> users = new ArrayList<>();

    private int trangThai;

    @Override
    @Transient
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Truong truong = (Truong) o;
        return getId() != null && Objects.equals(getId(), truong.getId());
    }

    @Override
    @Transient
    public int hashCode() {
        return getClass().hashCode();
    }
}