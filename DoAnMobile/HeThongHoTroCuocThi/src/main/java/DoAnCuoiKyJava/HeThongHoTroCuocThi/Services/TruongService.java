package DoAnCuoiKyJava.HeThongHoTroCuocThi.Services;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.LoaiTruong;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.Truong;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.ITruongRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TruongService {
    private final ITruongRepository truongRepository;

    public List<Truong> getAllTruongs() {
        return truongRepository.findAll();
    }

    public List<Truong> getAllTruongsHien() {
        return truongRepository.findByTrangThai(1);
    }

    public Optional<Truong> getTruongById(Long id) {
        return truongRepository.findById(id);
    }

    public Truong findTruongById(Long id) {
        return truongRepository.findTruongById(id);
    }

    public Truong addTruong(Truong truong) {
        truong.setTrangThai(1);
        return truongRepository.save(truong);
    }

    public Truong updateTruong(Truong updatedTruong) {
        Truong truong = getTruongById(updatedTruong.getId())
                .orElseThrow(() -> new EntityNotFoundException("Truong not found with id"));
        truong.setTenTruong(updatedTruong.getTenTruong());
        truong.setLoaiTruong(updatedTruong.getLoaiTruong());
        return truongRepository.save(truong);
    }

    public void AnHien(Long id) {
        Truong truong = truongRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Truong not found with id: " + id));

        truong.setTrangThai(truong.getTrangThai() == 0 ? 1 : 0);
        truongRepository.save(truong);
    }

    /*----------------------------------------------------------*/

}