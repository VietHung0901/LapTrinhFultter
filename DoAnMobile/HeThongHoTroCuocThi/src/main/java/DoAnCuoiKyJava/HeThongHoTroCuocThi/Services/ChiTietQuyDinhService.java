package DoAnCuoiKyJava.HeThongHoTroCuocThi.Services;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.*;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IChiTietQuyDinhRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChiTietQuyDinhService {
    private final IChiTietQuyDinhRepository CTQDRepository;

    public List<ChiTietQuyDinh> getAllCTQD() {
        return CTQDRepository.findAll();
    }

    public ChiTietQuyDinh addChiTietQuyDinh(ChiTietQuyDinh CTQD) {
        return CTQDRepository.save(CTQD);
    }

    public List<QuyDinh> getAllQuyDinhByCuocThi(CuocThi cuocThi) {
        List<ChiTietQuyDinh> chiTietQuyDinhs = getAllCTQD();
        List<QuyDinh> quyDinhs = new ArrayList<>();
        for (ChiTietQuyDinh chiTietQuyDinh : chiTietQuyDinhs) {
            if(chiTietQuyDinh.getCuocThi() == cuocThi) {
                quyDinhs.add(chiTietQuyDinh.getQuyDinh());
                //Xóa, khi sửa sẽ thêm vào lại
//                CTQDRepository.delete(chiTietQuyDinh);
            }
        }
        return quyDinhs;
    }

    public void deleteAllQuyDinhByCuocThi(Long cuocThiId) {
        for (ChiTietQuyDinh chiTietQuyDinh : getAllCTQD()) {
            if(chiTietQuyDinh.getCuocThi().getId() == cuocThiId) {
                //Xóa, khi sửa sẽ thêm vào lại
                CTQDRepository.delete(chiTietQuyDinh);
            }
        }
    }

    public List<ChiTietQuyDinh> getChiTietQuyDinhsByCuocThiId(Long cuocThiId) {
        return CTQDRepository.findByCuocThiId(cuocThiId);
    }

    public Page<ChiTietQuyDinh> getChiTietQuyDinhsByCuocThiId(Long cuocThiId, Pageable pageable) {
        return CTQDRepository.findByCuocThiId(cuocThiId, pageable);
    }


}
