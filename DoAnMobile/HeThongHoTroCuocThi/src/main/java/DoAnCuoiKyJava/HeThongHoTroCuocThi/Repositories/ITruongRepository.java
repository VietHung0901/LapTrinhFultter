package DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.Truong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITruongRepository extends JpaRepository<Truong, Long> {
    //Xuất các loại trường đang không bị ẩn
    List<Truong> findByTrangThai(int trangThai);

    Truong findTruongById(Long id);
    Truong findByTenTruong(String tenTruong);

    /*---------------------------------------------------------------------------*/

}