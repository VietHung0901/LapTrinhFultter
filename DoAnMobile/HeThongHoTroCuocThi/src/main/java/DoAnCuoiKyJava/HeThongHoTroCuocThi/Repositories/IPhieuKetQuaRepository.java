package DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuDangKy;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuKetQua;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPhieuKetQuaRepository extends JpaRepository<PhieuKetQua, Long> {
//    Optional<PhieuKetQua> findByPhieuDangKyAndTrangThai(PhieuDangKy phieuDangKy, int trangThai);
    List<PhieuKetQua> findByTrangThai(int trangThai);
    PhieuKetQua findByPhieuDangKy(PhieuDangKy phieuDangKy);

    List<PhieuKetQua> findByPhieuDangKyUserTruongId(Long truongId);

    @Query("SELECT pkq FROM PhieuKetQua pkq " +
            "WHERE pkq.phieuDangKy.truongId = :truongId " +
            "AND pkq.phieuDangKy.cuocThi.id = :cuocThiId")
    List<PhieuKetQua> findAllByTruongIdAndCuocThiId(@Param("truongId") Long truongId, @Param("cuocThiId") Long cuocThiId);


    /*------------------------------- Xuat danh sach ket qua thi sinh --------------------------------------------*/
    @Query("SELECT p FROM PhieuKetQua p WHERE p.phieuDangKy.cuocThi = :cuocThi AND p.phieuDangKy.user.truong.id = :truongId")
    List<PhieuKetQua> findByCuocThiAndTruong(@Param("cuocThi") CuocThi cuocThi, @Param("truongId") Long truongId);


}