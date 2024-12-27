package DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.ChiTietNoiDung;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.NoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IChiTietNoiDungRepository extends JpaRepository<ChiTietNoiDung, Long>{
    List<NoiDung> findAllNoiDungByCuocThi(CuocThi cuocThi);
    List<ChiTietNoiDung> findByCuocThiId(Long cuocThiId);
}
