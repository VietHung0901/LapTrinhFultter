package DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ICuocThiRepository extends JpaRepository<CuocThi, Long> {
    //Xuất các loại trường đang không bị ẩn
    List<CuocThi> findByTrangThai(int trangThai);
}