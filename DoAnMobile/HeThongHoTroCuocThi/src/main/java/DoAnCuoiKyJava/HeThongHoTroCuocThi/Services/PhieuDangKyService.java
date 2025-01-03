package DoAnCuoiKyJava.HeThongHoTroCuocThi.Services;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.*;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Request.PhieuKetQuaRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IPhieuDangKyRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.ITruongRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Request.PhieuDangKyCreate;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhieuDangKyService {
    private final IPhieuDangKyRepository phieuDangKyRepository;
    private final UserService userService;
    private final ITruongRepository truongRepository;

    public List<PhieuDangKy> getAllPhieuDangKys() {
        return phieuDangKyRepository.findAll();
    }

    public List<PhieuDangKy> getAllPhieuDangKystheoCuocThi(Long cuocThiId) {
        return phieuDangKyRepository.findByCuocThi_Id(cuocThiId);
    }

    public Optional<PhieuDangKy> getPhieuDangKyById(Long id) {
        return phieuDangKyRepository.findById(id);
    }

    public PhieuDangKy addPhieuDangKy(PhieuDangKy phieuDangKy) {
        phieuDangKy.setTrangThai(1);
        return phieuDangKyRepository.save(phieuDangKy);
    }

    public PhieuDangKy updatePhieuDangKy(PhieuDangKy updatedPhieuDangKy) {
        PhieuDangKy phieuDangKy = getPhieuDangKyById(updatedPhieuDangKy.getId())
                .orElseThrow(() -> new EntityNotFoundException("Truong not found with id"));
        phieuDangKy.setEmail(updatedPhieuDangKy.getEmail());
        phieuDangKy.setSdt(updatedPhieuDangKy.getSdt());
        phieuDangKy.setTruongId(updatedPhieuDangKy.getTruongId());
        return phieuDangKyRepository.save(phieuDangKy);
    }

    //Đếm phiếu đăng ký theo cuộc thi
    public int countPhieuDangKyByCuocThiId(Long cuocThiId) {
        return phieuDangKyRepository.countByCuocThiId(cuocThiId);
    }

    public PhieuDangKy mapToPhieuDangKy (PhieuDangKyCreate phieuDangKyCreate)
    {
        PhieuDangKy phieuDangKy = new PhieuDangKy();
        phieuDangKy.setCuocThi(phieuDangKyCreate.getCuocThi());
        phieuDangKy.setSdt(phieuDangKyCreate.getSdt());
        phieuDangKy.setEmail(phieuDangKyCreate.getEmail());
        phieuDangKy.setTruongId(phieuDangKyCreate.getTruongId());
        phieuDangKy.setNgayDangKy(LocalDateTime.now());
        User user = userService.findById(phieuDangKyCreate.getUserId());
        phieuDangKy.setUser(user);

        return phieuDangKy;
    }

    public boolean tonTaiPhieuDangKyUserId_CuocThiId (Long userId, Long cuocThiId)
    {
        List<PhieuDangKy> listPDK = getAllPhieuDangKys();
        for (PhieuDangKy pdk : listPDK) {
            if(pdk.getUser().getId() == userId && pdk.getCuocThi().getId() == cuocThiId)
                return true;
        }
        return false;
    }

    public List<PhieuDangKy> getPdkByUser (User user)
    {
        List<PhieuDangKy> listPDK = new ArrayList<>();
        for (PhieuDangKy pdk : getAllPhieuDangKys()) {
            if(pdk.getUser() == user)
            {
                listPDK.add(pdk);
            }
        }
        return listPDK;
    }

    public int getTotalPDKs() {
        List<PhieuDangKy> allPhieuDangKys = phieuDangKyRepository.findAll();
        return allPhieuDangKys.size();
    }

    // Hàm import điểm từ file excel
    public List<String[]> importPhieuDangKyFromExcel(MultipartFile file, CuocThi cuocThi) throws IOException {
        List<String[]> listFail = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            var sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                User user = new User();
                Row row = sheet.getRow(i);
                if (row != null) {
                    String cccd = dataFormatter.formatCellValue(row.getCell(1));
                    if(!cccd.isEmpty()) {
                        user = userService.getUserByCCCD(cccd).orElseThrow(() -> new EntityNotFoundException("User not found with CCCD: " + cccd));

                        PhieuDangKy pdkCheck = phieuDangKyRepository.findByCuocThiAndUser(cuocThi, user);
                        if (pdkCheck == null) {
                            PhieuDangKy pdk = new PhieuDangKy();
                            pdk.setUser(user);
                            pdk.setCuocThi(cuocThi);
                            pdk.setSdt(dataFormatter.formatCellValue(row.getCell(5)));
                            pdk.setEmail(dataFormatter.formatCellValue(row.getCell(4)));

                            Truong truong = truongRepository.findByTenTruong(dataFormatter.formatCellValue(row.getCell(6)));
                            pdk.setTruongId(truong.getId());

                            pdk.setNgayDangKy(LocalDateTime.now());
                            pdk.setTrangThai(1);
                            phieuDangKyRepository.save(pdk);
                        } else {
                            String[] failRow = new String[row.getPhysicalNumberOfCells()];
                            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                                failRow[j] = dataFormatter.formatCellValue(row.getCell(j));
                            }
                            listFail.add(failRow);
                        }
                    }else
                        return listFail;
                }
            }
        }
        return listFail;
    }

    public List<Integer> kiemTraThongTin(List<PhieuKetQuaRequest> list, Long cuocThiId) {
        List<Integer> listKetQua = new ArrayList<>();
        List<PhieuDangKy> listPDKCuocThi = getAllPhieuDangKystheoCuocThi(cuocThiId);
        // Duyệt qua từng PhieuDangKy trong danh sách
        for (PhieuKetQuaRequest pkq : list) {
            int ketQua = 3; // Không xác định
            for(PhieuDangKy pdkCuocThi: listPDKCuocThi)
                // Kiểm tra tồn tại mã phiếu trong cuộc thi không?
                if(pkq.getMaPhieu().equals(pdkCuocThi.getId().toString()))
                    // Nếu có mã phiếu trong cuộc thi sẽ kiểm tra tiếp đến thông tin (cccd và họ tên)
                    if (!pkq.getCccd().equals(pdkCuocThi.getUser().getCccd()) || !pkq.getHoTen().equals(pdkCuocThi.getUser().getHoten())) {
                        ketQua = 2; // Sai thông tin nếu không khớp CCCD hoặc Họ tên
                    } else {
                        ketQua = 1; // Nếu mọi thứ khớp, thì kết quả là chính xác
                        break; // Thoát khỏi vòng lặp nếu đã tìm thấy kết quả chính xác
                    }
            listKetQua.add(ketQua);
        }
        return listKetQua;
    }
}