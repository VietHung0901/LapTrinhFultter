package DoAnCuoiKyJava.HeThongHoTroCuocThi.Services;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.*;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IPhieuDangKyRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IPhieuKetQuaRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Request.PhieuKetQuaRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PhieuKetQuaService {
    private final IPhieuKetQuaRepository phieuKetQuaRepository;
    private final IPhieuDangKyRepository phieuDangKyRepository;

    //lấy các phiếu kết quả có trạng thái là 1 (Hiện)
    public List<PhieuKetQua> getAllPhieuKetQua() {
        return phieuKetQuaRepository.findByTrangThai(1);
    }

    public List<PhieuKetQua> getAllPhieuKetQuastheoCuocThi(CuocThi cuocThi) {
        List<PhieuKetQua> listPKQ = new ArrayList<>();
        for (PhieuKetQua pkq : getAllPhieuKetQua()) {
            if (pkq.getPhieuDangKy().getCuocThi() == cuocThi) {
                listPKQ.add(pkq);
            }
        }

        // Sắp xếp listPKQ giảm dần theo điểm, tăng dần theo phút, tăng dần theo giây
        Collections.sort(listPKQ, new Comparator<PhieuKetQua>() {
            @Override
            public int compare(PhieuKetQua pkq1, PhieuKetQua pkq2) {
                int diemCompare = Integer.compare(pkq2.getDiem(), pkq1.getDiem());
                if (diemCompare != 0) {
                    return diemCompare;
                }
                int phutCompare = Integer.compare(pkq1.getPhut(), pkq2.getPhut());
                if (phutCompare != 0) {
                    return phutCompare;
                }
                return Integer.compare(pkq1.getGiay(), pkq2.getGiay());
            }
        });
        return listPKQ;
    }


    public Optional<PhieuKetQua> getPhieuKetQuaById(Long id) {
        return phieuKetQuaRepository.findById(id);
    }

    public PhieuKetQua addPhieuKetQua(PhieuKetQua phieuKetQua) {
        phieuKetQua.setTrangThai(1);
        return phieuKetQuaRepository.save(phieuKetQua);
    }

    //khi sửa phiếu kết quả sẽ có trạng thái là 0
    public PhieuKetQua editPhieuKetQua(PhieuKetQua phieuKetQua) {
        phieuKetQua.setTrangThai(0);
        return phieuKetQuaRepository.save(phieuKetQua);
    }

    // Hàm lấy điểm theo cuộc thi và userId
    public String getDiemByCuocThiIdvaUserId(Long cuocThiId, Long userId) {
        for (PhieuKetQua pkq : getAllPhieuKetQua()) {
            if (pkq.getPhieuDangKy().getCuocThi().getId() == cuocThiId && pkq.getPhieuDangKy().getUser().getId() == userId) {
                return String.valueOf(pkq.getDiem());
            }
        }
        return null;
    }

    // Hàm Lấy PhieuKetQua theo pdk
    public PhieuKetQua getPhieuKetQuaByPhieuDangKyid(Long id) {
        PhieuDangKy phieuDangKy = phieuDangKyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(""));
        for (PhieuKetQua pkq : getAllPhieuKetQua()) {
            if (pkq.getPhieuDangKy() == phieuDangKy) {
                return pkq;
            }
        }
        return null;
    }

    // Hàm Lấy PhieuKetQua theo pdk
    public PhieuKetQua getPhieuKetQuaByPhieuDangKy(PhieuDangKy phieuDangKy) {
        for (PhieuKetQua pkq : getAllPhieuKetQua()) {
            if (pkq.getPhieuDangKy() == phieuDangKy) {
                return pkq;
            }
        }
        return null;
    }

    // Hàm để cập nhật PhieuKetQua
    public PhieuKetQua updatePhieuKetQua(PhieuKetQua updatephieuKetQua) {
        PhieuKetQua pkq = getPhieuKetQuaById(updatephieuKetQua.getId()).orElseThrow(() -> new EntityNotFoundException(""));
        pkq.setDiem(updatephieuKetQua.getDiem());
        pkq.setPhut(updatephieuKetQua.getPhut());
        pkq.setGiay(updatephieuKetQua.getGiay());
        return phieuKetQuaRepository.save(pkq);
    }

    // Hàm chuyển kiểu dữ liệu từ PhieuKetQua sang PhieuKetQuaRequest
    public PhieuKetQuaRequest mapToPhieuKetQuaRequest(PhieuKetQua phieuKetQua) {
        PhieuKetQuaRequest phieuKetQuaRequest = new PhieuKetQuaRequest();
        phieuKetQuaRequest.setId(phieuKetQua.getId());
        phieuKetQuaRequest.setPhut(phieuKetQua.getPhut());
        phieuKetQuaRequest.setGiay(phieuKetQua.getGiay());
        phieuKetQuaRequest.setDiem(phieuKetQua.getDiem());
        phieuKetQuaRequest.setPhieuDangKy(phieuKetQua.getPhieuDangKy());
        return phieuKetQuaRequest;
    }

    // Hàm lấy danh sách pkq theo user
    public List<PhieuKetQua> getPkqByUser(User user) {
        List<PhieuKetQua> listPKQ = new ArrayList<>();
        for (PhieuKetQua pkq : getAllPhieuKetQua()) {
            if (pkq.getPhieuDangKy().getUser() == user) {
                listPKQ.add(pkq);
            }
        }
        return listPKQ;
    }

    // Hàm lấy danh sách pkq theo user và trạng thái
    public List<PhieuKetQua> GetAllPKQByUserAndTrangThai(User user) {
        List<PhieuKetQua> listPKQ = new ArrayList<>();
        for (PhieuKetQua pqk : getAllPhieuKetQua())
            if (pqk.getPhieuDangKy().getUser() == user)
                listPKQ.add(pqk);
        return listPKQ;
    }

    // Hàm import điểm từ file excel
    public List<String[]> importPhieuSuaDiemFromExcel(MultipartFile file) throws IOException {
        List<String[]> listFail = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            var sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {

                    // Kiểu dữ liệu khi lấy từ file vào là string cần chuyển thành kiểu dữ liệu phù hợp
                    String pdkIdString = dataFormatter.formatCellValue(row.getCell(0)); // Chuỗi kết quả
                    if (!pdkIdString.isEmpty()) {
                        Long pdkId = Long.parseLong(pdkIdString); // Chuyển chuỗi sang kiểu Long
                        PhieuDangKy pdk = phieuDangKyRepository.findById(pdkId).orElseThrow(() -> new EntityNotFoundException("PhieuDangKy not found with id: " + pdkId));

                        PhieuKetQua pkqCheck = getPhieuKetQuaByPhieuDangKy(pdk);
                        // **Kiểm tra danh sách pkq Nếu tồn tại pdkID != pkq.getPdkId thì thực hiện lưu
                        if (pkqCheck == null) {

                            PhieuKetQua pkq = new PhieuKetQua();
                            // Nối pdk với pkq
                            pkq.setPhieuDangKy(pdk);
                            pkq.setPhut(Integer.parseInt(dataFormatter.formatCellValue(row.getCell(5))));
                            pkq.setGiay(Integer.parseInt(dataFormatter.formatCellValue(row.getCell(6))));
                            pkq.setDiem(Integer.parseInt(dataFormatter.formatCellValue(row.getCell(7))));
                            pkq.setTrangThai(1);
                            phieuKetQuaRepository.save(pkq);
                        } else { // **Ngược lại nếu tồn tại pdkID != pkq.getPdkId thì thông báo
                            String[] failRow = new String[row.getPhysicalNumberOfCells()];
                            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                                failRow[j] = dataFormatter.formatCellValue(row.getCell(j));
                            }
                            listFail.add(failRow);
                        }
                    } else
                        return listFail;
                }
            }
        }
        return listFail;
    }
// import file excel    *********************************************************


    /*------------------------------- Xuat danh sach ket qua thi sinh --------------------------------------------*/
    public List<PhieuKetQua> getPhieuKetQuaTheoTruongVaCuocThi(Long truongId, Long cuocThiId) {
        return phieuKetQuaRepository.findAllByTruongIdAndCuocThiId(truongId, cuocThiId);
    }

    public List<PhieuKetQua> getPhieuKetQuaByCuocThiAndTruong(CuocThi cuocThi, Long truongId) {
        return phieuKetQuaRepository.findByCuocThiAndTruong(cuocThi, truongId);
    }

    public PhieuKetQua mapPhieuKetQuaRequestToPhieuKetQua(DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Request.PhieuKetQuaRequest phieuKetQua) {
        PhieuKetQua pkq = new PhieuKetQua();

        pkq.setPhut(phieuKetQua.getPhut());
        pkq.setGiay(phieuKetQua.getGiay());
        pkq.setDiem(phieuKetQua.getDiem());
        String pdkIdString = phieuKetQua.getMaPhieu(); // Lấy giá trị kiểu String§
        Long pdkId = Long.parseLong(pdkIdString); // Chuyển String sang long§
        PhieuDangKy pdk = phieuDangKyRepository.findById(pdkId).orElseThrow(() -> new EntityNotFoundException("PhieuDangKy not found with id: " + pdkId));
        pkq.setPhieuDangKy(pdk);
        return pkq;
    }

    // Lưu danh sách phiếu kết quả vào cơ sở dữ liệu
    public void luuDanhSachPhieuKetQua(List<DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Request.PhieuKetQuaRequest> phieuKetQuaList) {
        for(DoAnCuoiKyJava.HeThongHoTroCuocThi.Flutter.Request.PhieuKetQuaRequest pkqRequest: phieuKetQuaList)
        {
            PhieuKetQua pkq = mapPhieuKetQuaRequestToPhieuKetQua(pkqRequest);
            pkq.setTrangThai(1);
            phieuKetQuaRepository.save(pkq);
        }
    }


}
