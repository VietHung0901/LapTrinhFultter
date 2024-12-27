package DoAnCuoiKyJava.HeThongHoTroCuocThi.Services;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuSuaDiem;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuKetQua;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IPhieuSuaDiemRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IPhieuKetQuaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhieuSuaDiemService {
    private final IPhieuSuaDiemRepository phieuSuaDiemRepository;
    private final IPhieuKetQuaRepository phieuKetQuaRepository;
    private final PhieuKetQuaService phieuKetQuaService;

    public List<PhieuSuaDiem> getAllPhieuSuaDiem() {return phieuSuaDiemRepository.findAll();}

    //lấy các PhieuSuaDiem theo trạng thái trạng thái (2-Chờ duyệt, 1-đồng ý, 0-không đồng ý)
    public List<PhieuSuaDiem> findPhieuSuaDiemsByTrangThai(int trangThai) {
        return phieuSuaDiemRepository.findPhieuSuaDiemsByTrangThai(trangThai);
    }

    // Tạo phiếu sửa điểm
    public PhieuSuaDiem createPhieuSuaDiem(Long phieuKetQuaCuId, Long phieuKetQuaMoiId, String lyDo, User user) {
        PhieuKetQua phieuKetQuaCu = phieuKetQuaRepository.findById(phieuKetQuaCuId)
                .orElseThrow(() -> new RuntimeException("Phiếu kết quả cũ không tồn tại"));

        PhieuKetQua phieuKetQuaMoi = phieuKetQuaRepository.findById(phieuKetQuaMoiId)
                .orElseThrow(() -> new RuntimeException("Phiếu kết quả mới không tồn tại"));

        PhieuSuaDiem phieuSuaDiem = new PhieuSuaDiem();
        phieuSuaDiem.setNgaySua(LocalDateTime.now());
        phieuSuaDiem.setLyDoSua(lyDo);
        phieuSuaDiem.setNguoiSua(user);
        phieuSuaDiem.setPhieuKetQuaCu(phieuKetQuaCu);
        phieuSuaDiem.setPhieuKetQuaMoi(phieuKetQuaMoi);
        phieuSuaDiem.setTrangThai(2);
        return phieuSuaDiemRepository.save(phieuSuaDiem);
    }

    // Lấy phiếu sửa điểm theo ID
    public PhieuSuaDiem getPhieuSuaDiemById(Long id) {
        return phieuSuaDiemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phiếu sửa điểm không tồn tại"));
    }

    public void xuLyKetQuaSuaDiem(Long id,int trangthai)
    {
        PhieuSuaDiem psd = getPhieuSuaDiemById(id);
        PhieuKetQua pkqCu = phieuKetQuaService.getPhieuKetQuaById(psd.getPhieuKetQuaCu().getId()).orElseThrow();
        PhieuKetQua pkqMoi = phieuKetQuaService.getPhieuKetQuaById(psd.getPhieuKetQuaMoi().getId()).orElseThrow();
        pkqCu.setTrangThai(trangthai != 1 ? 1 : 0);
        pkqMoi.setTrangThai(trangthai == 1 ? 1 : 0);
        phieuKetQuaRepository.save(pkqCu);
        phieuKetQuaRepository.save(pkqMoi);
        psd.setTrangThai(trangthai);
        phieuSuaDiemRepository.save(psd);
    }

    public void duyetAllPSD(){
        for(PhieuSuaDiem psd : phieuSuaDiemRepository.findPhieuSuaDiemsByTrangThai(2)){
            psd.setTrangThai(1);
            phieuSuaDiemRepository.save(psd);
        }
    }

}