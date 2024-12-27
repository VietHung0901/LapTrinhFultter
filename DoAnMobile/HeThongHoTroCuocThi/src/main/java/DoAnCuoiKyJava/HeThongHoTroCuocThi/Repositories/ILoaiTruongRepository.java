package DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.LoaiTruong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILoaiTruongRepository  extends JpaRepository<LoaiTruong, Long>{
    LoaiTruong findLoaiTruongById(Long id);

    //Xuất các loại trường đang không bị ẩn
    List<LoaiTruong> findByTrangThai(int trangThai);


}
