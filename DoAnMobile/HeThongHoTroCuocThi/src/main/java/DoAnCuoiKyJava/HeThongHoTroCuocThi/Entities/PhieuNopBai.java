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
@Table(name = "PhieuNopBai")
public class PhieuNopBai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileUrl;  // Đường dẫn tới file bài tập User đã nộp

    @ManyToOne
    @JoinColumn(name = "noi_dung_id")
    private NoiDung noiDung;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long cuocThiId;

    private int trangThai;

    private String nhanXet;

    private int diem;
}
