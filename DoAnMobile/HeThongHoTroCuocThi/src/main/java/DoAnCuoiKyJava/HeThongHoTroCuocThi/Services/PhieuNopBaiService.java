package DoAnCuoiKyJava.HeThongHoTroCuocThi.Services;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.NoiDung;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuNopBai;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IPhieuNopBaiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhieuNopBaiService {

    private final IPhieuNopBaiRepository phieuNopBaiRepository;

    public List<PhieuNopBai> findAll() {
        return phieuNopBaiRepository.findAll();
    }

    public List<PhieuNopBai> findAllByTrangThai(int trangthai) {
        return phieuNopBaiRepository.findPhieuNopBaiByTrangThai(trangthai);
    }

    public Optional<PhieuNopBai> findById(Long id) {
        return phieuNopBaiRepository.findById(id);
    }

    public PhieuNopBai add(PhieuNopBai phieuNopBai, MultipartFile UrlFile) {
        if (!UrlFile.isEmpty()) {
            String fileUrl = saveFile(UrlFile);
            phieuNopBai.setFileUrl(fileUrl);
        }
        phieuNopBai.setTrangThai(0);
        return phieuNopBaiRepository.save(phieuNopBai);
    }

    public PhieuNopBai edit(PhieuNopBai phieuNopBai) {
        PhieuNopBai pnb = findById(phieuNopBai.getId()).get();
        if(pnb != null){
            pnb.setNhanXet(phieuNopBai.getNhanXet());
            pnb.setDiem(phieuNopBai.getDiem());
            pnb.setTrangThai(1);
        }
        return phieuNopBaiRepository.save(pnb);
    }

    public String saveFile(MultipartFile file) {
        // Xử lý lưu file vào thư mục cụ thể và trả về đường dẫn
        String uploadDir = "F:\\DACN_HeThongMOS\\HeThongHoTroCuocThi\\src\\main\\resources\\static\\uploads\\Result\\";
        /*String uploadDir = "/Users/tranviethung/Documents/Học tập/HeThongHoTroCuocThiJaVa/HeThongHoTroCuocThi/src/main/resources/static/uploads/Result/";*/
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu file: " + fileName, e);
        }

        return "/uploads/Result/" + fileName;
    }

    public PhieuNopBai findPhieuNopBaiByCuocThiIdAndNoiDungAndUser(Long cuocThiId, NoiDung noiDung, User user){
        List<PhieuNopBai> list = findAll();
        for (PhieuNopBai phieuNopBai : list) {
            if(phieuNopBai.getCuocThiId() == cuocThiId)
                if(phieuNopBai.getNoiDung() == noiDung)
                    if (phieuNopBai.getUser() == user)
                        return phieuNopBai;
        }
        return null;
    }
}