package DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.MonThi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMonThiRepository extends JpaRepository<MonThi, Long> {
    //Xuất các loại trường đang không bị ẩn
    List<MonThi> findByTrangThai(int trangThai);
}