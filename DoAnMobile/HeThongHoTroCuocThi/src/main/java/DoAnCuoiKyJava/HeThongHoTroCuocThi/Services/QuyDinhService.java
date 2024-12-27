package DoAnCuoiKyJava.HeThongHoTroCuocThi.Services;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.QuyDinh;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IQuyDinhRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Request.QuyDinhCreateRequest;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class QuyDinhService {

    private final IQuyDinhRepository quyDinhRepository;

    public List<QuyDinh> getAllQuyDinhs() {
        return quyDinhRepository.findAll();
    }

    public List<QuyDinh> getAllQuyDinhsHien() {
        return quyDinhRepository.findByTrangThai(1);
    }

    public Optional<QuyDinh> getQuyDinhById(Long id) {
        return quyDinhRepository.findById(id);
    }

    public void addQuyDinh(QuyDinh quyDinh) {
        quyDinh.setTrangThai(1);
        quyDinhRepository.save(quyDinh);
    }

    public void updateQuyDinh(@NotNull QuyDinh quyDinh) {
        QuyDinh existingQuyDinh = quyDinhRepository.findById(quyDinh.getId()).orElse(null);
        if (existingQuyDinh != null) {
            existingQuyDinh.setTenQuyDinh(quyDinh.getTenQuyDinh());
            existingQuyDinh.setMoTaQuyDinh(quyDinh.getMoTaQuyDinh());
            existingQuyDinh.setImageUrl(quyDinh.getImageUrl());
            quyDinhRepository.save(existingQuyDinh);
        }
    }

    //hàm chuyển từ QuyDinhCreateRequest sang QuyDinh
    public QuyDinh mapToQuyDinh(QuyDinhCreateRequest quyDinhCreateRequest) {
        QuyDinh quyDinh = new QuyDinh();
        quyDinh.setId(quyDinhCreateRequest.getId());
        quyDinh.setTenQuyDinh(quyDinhCreateRequest.getTenQuyDinh());
        quyDinh.setMoTaQuyDinh(quyDinhCreateRequest.getMoTaQuyDinh());
        if (!quyDinhCreateRequest.getImageUrl().isEmpty()) {
            String image = saveImage(quyDinhCreateRequest.getImageUrl());
            quyDinh.setImageUrl(image);
        }
        return quyDinh;
    }

    public String saveImage(MultipartFile file) {
        // Lấy tên file
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Đường dẫn lưu file
        String uploadDir = "F:\\DACN_HeThongMOS\\HeThongHoTroCuocThi\\src\\main\\resources\\static\\images\\";
        Path filePath = Paths.get(uploadDir, fileName);

        try {
            // Kiểm tra xem thư mục có tồn tại không
            Files.createDirectories(Paths.get(uploadDir));

            // Lưu file vào thư mục
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not save file: " + fileName, e);
        }

        // Trả về đường dẫn của file đã lưu
        return "/images/" + fileName;
    }

    public void AnHien(Long id) {
        QuyDinh quyDinh = quyDinhRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));

        quyDinh.setTrangThai(quyDinh.getTrangThai() == 0 ? 1 : 0);
        quyDinhRepository.save(quyDinh);
    }
}
