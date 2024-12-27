package DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.NoiDung;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuNopBai;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPhieuNopBaiRepository extends JpaRepository<PhieuNopBai, Long> {
    // Bạn có thể thêm các phương thức truy vấn tùy chỉnh nếu cần
    List<PhieuNopBai> findPhieuNopBaiByTrangThai(int trangThai);

    PhieuNopBai findPhieuNopBaiByCuocThiIdAndNoiDungAndUser(Long cuocThiId, NoiDung noiDung, User user);
}