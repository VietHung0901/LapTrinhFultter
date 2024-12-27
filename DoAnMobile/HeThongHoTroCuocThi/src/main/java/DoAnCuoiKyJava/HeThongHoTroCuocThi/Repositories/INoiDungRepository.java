package DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.NoiDung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface INoiDungRepository extends JpaRepository<NoiDung, Long> {
    List<NoiDung> findByTrangThai(int trangThai);
}
