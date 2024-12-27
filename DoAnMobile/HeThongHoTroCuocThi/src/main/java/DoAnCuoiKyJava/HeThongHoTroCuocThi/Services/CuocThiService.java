package DoAnCuoiKyJava.HeThongHoTroCuocThi.Services;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.ChiTietNoiDung;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.ICuocThiRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Request.CuocThiCreateRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CuocThiService {
    private final ICuocThiRepository cuocThiRepository;

    public List<CuocThi> getAllCuocThis() {
        return cuocThiRepository.findAll();
    }

    public List<CuocThi> getAllCuocThisHien() {
        return cuocThiRepository.findByTrangThai(1);
    }

    public Optional<CuocThi> getCuocThiById(Long id) {
        return cuocThiRepository.findById(id);
    }

    public CuocThi addCuocThi(CuocThi cuocThi) {
        cuocThi.setTrangThai(1);
        return cuocThiRepository.save(cuocThi);
    }

    public CuocThi updateCuocThi(CuocThi updatedCuocThi) {
        CuocThi cuocThi = getCuocThiById(updatedCuocThi.getId())
                .orElseThrow(() -> new EntityNotFoundException("CuocThi not found with id"));
        cuocThi.setTenCuocThi(updatedCuocThi.getTenCuocThi());
        cuocThi.setNgayThi(updatedCuocThi.getNgayThi());
        cuocThi.setSoLuongThiSinh(updatedCuocThi.getSoLuongThiSinh());
        cuocThi.setDiaDiemThi(updatedCuocThi.getDiaDiemThi());
        cuocThi.setMonThi(updatedCuocThi.getMonThi());
        cuocThi.setLoaiTruongId(updatedCuocThi.getLoaiTruongId());
        return cuocThiRepository.save(cuocThi);
    }

    public void AnHien(Long id) {
        CuocThi cuocThi = cuocThiRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CuocThi not found with id: " + id));

        cuocThi.setTrangThai(cuocThi.getTrangThai() == 0 ? 1 : 0);
        cuocThiRepository.save(cuocThi);
    }

    private CuocThi mapToCuocThi(CuocThiCreateRequest cuocThiRequest) {
        CuocThi cuocThi = new CuocThi();
        cuocThi.setTenCuocThi(cuocThiRequest.getTenCuocThi());
        cuocThi.setNgayThi(cuocThiRequest.getNgayThi());
        cuocThi.setSoLuongThiSinh(cuocThiRequest.getSoLuong());
        cuocThi.setDiaDiemThi(cuocThiRequest.getDiaDiemThi());
        cuocThi.setMonThi(cuocThiRequest.getMonThi());
        return cuocThi;
    }

    public List<CuocThi> searchByNgayThi(LocalDate startDate, LocalDate endDate, List<CuocThi> list) {
        List<CuocThi> listCuocThi = new ArrayList<>();
        for (CuocThi cuocThi : list) {
            LocalDate cuocThiDate = cuocThi.getNgayThi().toLocalDate();
            if (cuocThiDate.compareTo(startDate) >= 0 && cuocThiDate.compareTo(endDate) <= 0) {
                listCuocThi.add(cuocThi);
            }
        }
        return listCuocThi;
    }

    public List<CuocThi> searchByDiadime (String diadiem, List<CuocThi> list)
    {
        List<CuocThi> listCuocThi = new ArrayList<>();
        for (CuocThi cuocThi : list) {
            if (cuocThi.getDiaDiemThi().contains(diadiem)) {
                listCuocThi.add(cuocThi);
            }
        }
        return listCuocThi;
    }

    public List<CuocThi> searchByMonThi (Long monThiId, List<CuocThi> list)
    {
        List<CuocThi> listCuocThi = new ArrayList<>();
        for (CuocThi cuocThi : list) {
            if(cuocThi.getMonThi().getId() == monThiId)
                listCuocThi.add(cuocThi);
        }
        return listCuocThi;
    }

    public List<CuocThi> searchByLoaiTruong (Long loaiTruongId, List<CuocThi> list)
    {
        List<CuocThi> listCuocThi = new ArrayList<>();
        for (CuocThi cuocThi : list) {
            if(cuocThi.getLoaiTruongId() == loaiTruongId)
                listCuocThi.add(cuocThi);
        }
        return listCuocThi;
    }

    public int getTotalCuocThis() {
        return cuocThiRepository.findAll().size();
    }

}