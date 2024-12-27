package DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.QuyDinh;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IQuyDinhRepository extends JpaRepository<QuyDinh, Long> {
    List<QuyDinh> findByTrangThai(int trangThai);
}
