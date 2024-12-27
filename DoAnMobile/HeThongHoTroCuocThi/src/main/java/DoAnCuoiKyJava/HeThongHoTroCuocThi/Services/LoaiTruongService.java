package DoAnCuoiKyJava.HeThongHoTroCuocThi.Services;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.LoaiTruong;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.ILoaiTruongRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoaiTruongService {
    private final ILoaiTruongRepository loaiTruongRepository;

    public List<LoaiTruong> getAllLoaiTruongs() {
        return loaiTruongRepository.findAll();
    }

    public List<LoaiTruong> getAllLoaiTruongsHien() {
        return loaiTruongRepository.findByTrangThai(1);
    }

    public LoaiTruong getLoaiTruongById(Long id) {
        return loaiTruongRepository.findLoaiTruongById(id);
    }

    public LoaiTruong addLoaiTruong(LoaiTruong loaiTruong) {
        loaiTruong.setTrangThai(1);
        return loaiTruongRepository.save(loaiTruong);
    }

    public LoaiTruong updateLoaiTruong(LoaiTruong updatedLoaiTruong) {
        LoaiTruong loaiTruong = getLoaiTruongById(updatedLoaiTruong.getId());
        loaiTruong.setTenLoaiTruong(updatedLoaiTruong.getTenLoaiTruong());
        return loaiTruongRepository.save(loaiTruong);
    }

    public void AnHien(Long id) {
        LoaiTruong loaiTruong = loaiTruongRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LoaiTruong not found with id: " + id));

        loaiTruong.setTrangThai(loaiTruong.getTrangThai() == 0 ? 1 : 0);
        loaiTruongRepository.save(loaiTruong);
    }

    public String getNameById(Long id) {
        LoaiTruong loaiTruong = loaiTruongRepository.findLoaiTruongById(id);
        if (loaiTruong != null) {
            return loaiTruong.getTenLoaiTruong();
        } else {
            return "null";
        }
    }
}