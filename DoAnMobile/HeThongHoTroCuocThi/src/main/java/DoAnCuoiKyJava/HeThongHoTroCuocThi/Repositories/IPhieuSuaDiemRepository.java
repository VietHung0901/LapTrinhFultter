package DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuSuaDiem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPhieuSuaDiemRepository extends JpaRepository<PhieuSuaDiem, Long> {
    List<PhieuSuaDiem> findPhieuSuaDiemsByTrangThai(int trangThai);
}