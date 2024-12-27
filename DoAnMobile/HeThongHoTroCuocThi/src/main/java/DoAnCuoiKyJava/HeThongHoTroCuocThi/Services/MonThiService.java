package DoAnCuoiKyJava.HeThongHoTroCuocThi.Services;


import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.MonThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IMonThiRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MonThiService {
    private final IMonThiRepository monThiRepository;

    public List<MonThi> getAllMonThis() {
        return monThiRepository.findAll();
    }

    public List<MonThi> getAllMonThisHien() {
        return monThiRepository.findByTrangThai(1);
    }

    public Optional<MonThi> getMonThiById(Long id) {
        return monThiRepository.findById(id);
    }

    public MonThi addMonThi(MonThi monThi) {
        monThi.setTrangThai(1);
        return monThiRepository.save(monThi);
    }

    public MonThi updateMonThi(MonThi updatedMonThi) {
        MonThi monThi = getMonThiById(updatedMonThi.getId())
                .orElseThrow(() -> new EntityNotFoundException("MonThi not found with id"));
        monThi.setTenMonThi(updatedMonThi.getTenMonThi());
        return monThiRepository.save(monThi);
    }

    public void AnHien(Long id) {
        MonThi monThi = monThiRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MonThi not found with id: " + id));

        monThi.setTrangThai(monThi.getTrangThai() == 0 ? 1 : 0);
        monThiRepository.save(monThi);
    }

}
