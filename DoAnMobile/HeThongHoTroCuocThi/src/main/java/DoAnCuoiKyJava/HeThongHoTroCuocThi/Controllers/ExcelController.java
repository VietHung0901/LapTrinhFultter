package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.CuocThi;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuDangKy;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.PhieuKetQua;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.Truong;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IPhieuKetQuaRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.io.ByteArrayOutputStream;

@Controller
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {
    private final PhieuKetQuaService phieuKetQuaService;
    private final CuocThiService cuocThiService;
    private final PhieuDangKyService phieuDangKyService;
    private final UserService userService;
    private final TruongService truongService;
    private final PhieuDangKyService phieuKyService;

    // Xuất danh sách kết quả theo cuộc thi
    @GetMapping("/export/diem/cuocThi/{id}")
    public ResponseEntity<byte[]> exportToExcelDiem(@PathVariable Long id) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // Tạo tiêu đề
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã phiếu");
        headerRow.createCell(1).setCellValue("Cuộc thi");
        headerRow.createCell(2).setCellValue("CCCD");
        headerRow.createCell(3).setCellValue("Họ và tên");
        headerRow.createCell(4).setCellValue("Email");
        headerRow.createCell(5).setCellValue("SĐT");
        headerRow.createCell(6).setCellValue("Trường");
        headerRow.createCell(7).setCellValue("Phút");
        headerRow.createCell(8).setCellValue("Giây");
        headerRow.createCell(9).setCellValue("Điểm");

        CuocThi cuocThi = cuocThiService.getCuocThiById(id)
                .orElseThrow(() -> new EntityNotFoundException("CuocThi not found with id: " + id));;

        List<PhieuKetQua> dataList = phieuKetQuaService.getAllPhieuKetQuastheoCuocThi(cuocThi);

        int rowNum = 1;
        for (PhieuKetQua data : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getId());
            row.createCell(1).setCellValue(data.getPhieuDangKy().getCuocThi().getTenCuocThi());
            row.createCell(2).setCellValue(data.getPhieuDangKy().getUser().getCccd());
            row.createCell(3).setCellValue(data.getPhieuDangKy().getUser().getHoten());
            row.createCell(4).setCellValue(data.getPhieuDangKy().getEmail());
            row.createCell(5).setCellValue(data.getPhieuDangKy().getSdt());

            Truong truong = truongService.findTruongById(data.getPhieuDangKy().getTruongId());
            row.createCell(6).setCellValue(truong.getTenTruong());

            row.createCell(7).setCellValue(data.getPhut());
            row.createCell(8).setCellValue(data.getGiay());
            row.createCell(9).setCellValue(data.getDiem());
        }

        // Xuất file Excel
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= DanSachPhieuKetQua" + "_" + cuocThi.getTenCuocThi() + "_" + cuocThi.getId() +".xlsx");
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Xuất danh sách Phiếu đăng ký theo cuộc thi
    @GetMapping("/export/pdk/cuocThi/{id}")
    public ResponseEntity<byte[]> exportToExcelPDK(@PathVariable Long id) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // Tạo tiêu đề
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã phiếu");
        headerRow.createCell(1).setCellValue("Cuộc thi");
        headerRow.createCell(2).setCellValue("CCCD");
        headerRow.createCell(3).setCellValue("Họ và tên");
        headerRow.createCell(4).setCellValue("Ngày sinh");
        headerRow.createCell(5).setCellValue("Email");
        headerRow.createCell(6).setCellValue("SĐT");
        headerRow.createCell(7).setCellValue("Trường");

        // Tạo CellStyle cho định dạng ngày
        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));


        // Thêm dữ liệu (ở đây giả sử bạn có một danh sách dữ liệu)
        List<PhieuDangKy> dataList = phieuDangKyService.getAllPhieuDangKystheoCuocThi(id);

        int rowNum = 1;
        for (PhieuDangKy data : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getId());
            row.createCell(1).setCellValue(data.getCuocThi().getTenCuocThi());
            row.createCell(2).setCellValue(data.getUser().getCccd());
            row.createCell(3).setCellValue(data.getUser().getHoten());

            // Định dạng ngày sinh
            Cell dateCell = row.createCell(4);
            dateCell.setCellValue(data.getUser().getNgaySinh()); // Ngày sinh kiểu java.util.Date hoặc java.time.LocalDate
            dateCell.setCellStyle(dateCellStyle);

            row.createCell(5).setCellValue(data.getEmail());
            row.createCell(6).setCellValue(data.getSdt());

            Truong truong = truongService.findTruongById(data.getTruongId());
            row.createCell(7).setCellValue(truong.getTenTruong());
        }

        CuocThi cuocThi = cuocThiService.getCuocThiById(id).orElseThrow(() -> new EntityNotFoundException("CuocThi not found with id: " + id));

        // Xuất file Excel
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= DanSachPhieuDangKy" + "_" + cuocThi.getTenCuocThi() + "_" + cuocThi.getId() +".xlsx");
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Xuất mẫu nhập kết quả theo cuộc thi
    @GetMapping("/export/mau/pkq/cuocThi/{id}")
    public ResponseEntity<byte[]> exportMauToExcelPKQ(@PathVariable Long id) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // Tạo tiêu đề
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã phiếu");
        headerRow.createCell(1).setCellValue("Cuộc thi");
        headerRow.createCell(2).setCellValue("CCCD");
        headerRow.createCell(3).setCellValue("Họ và tên");
        headerRow.createCell(4).setCellValue("Trường");
        headerRow.createCell(5).setCellValue("Phút");
        headerRow.createCell(6).setCellValue("Giây");
        headerRow.createCell(7).setCellValue("Điểm");

        // Tạo CellStyle cho định dạng ngày
        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));


        // Thêm dữ liệu (ở đây giả sử bạn có một danh sách dữ liệu)
        List<PhieuDangKy> dataList = phieuDangKyService.getAllPhieuDangKystheoCuocThi(id);

        int rowNum = 1;
        for (PhieuDangKy data : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getId());
            row.createCell(1).setCellValue(data.getCuocThi().getTenCuocThi());
            row.createCell(2).setCellValue(data.getUser().getCccd());
            row.createCell(3).setCellValue(data.getUser().getHoten());

            Truong truong = truongService.findTruongById(data.getTruongId());
            row.createCell(4).setCellValue(truong.getTenTruong());
        }

        CuocThi cuocThi = cuocThiService.getCuocThiById(id).orElseThrow(() -> new EntityNotFoundException("CuocThi not found with id: " + id));

        // Xuất file Excel
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= DanSachPhieuDangKy" + "_" + cuocThi.getTenCuocThi() + "_" + cuocThi.getId() +".xlsx");
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Xuất mẫu nhập danh sách thí sinh đăng ký theo cuộc thi
    @GetMapping("/export/mau/pdk/cuocThi/{id}")
    public ResponseEntity<byte[]> exportMauToExcelPDK(@PathVariable Long id) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // Tạo tiêu đề
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Cuộc thi");
        headerRow.createCell(1).setCellValue("CCCD");
        headerRow.createCell(2).setCellValue("Họ và tên");
        headerRow.createCell(3).setCellValue("Ngày sinh");
        headerRow.createCell(4).setCellValue("Email");
        headerRow.createCell(5).setCellValue("SĐT");
        headerRow.createCell(6).setCellValue("Trường");

        CuocThi cuocThi = cuocThiService.getCuocThiById(id).orElseThrow(() -> new EntityNotFoundException("CuocThi not found with id: " + id));

        // Xuất file Excel
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= DanSachPhieuDangKy" + "_" + cuocThi.getTenCuocThi() + "_" + cuocThi.getId() +".xlsx");
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // ************************ Thực hiện chức năng nhập Phiếu kết quả bằng fiel excel *****************************
    // Chuyển sang form import file
    @GetMapping("/import/form/pkq/cuocThiId/{id}")
    public String importFileExcelDPKQ(@PathVariable Long id, Model model) {
        model.addAttribute("cuocThiId", id);
        return "Admin/PhieuKetQua/import-FileExcel";
    }

    // Lưu dữ liệu từ file excel vào database
    @PostMapping("/import/form/pkq")
    public String importDSPKQ(@RequestParam("file") MultipartFile file,
                                 @RequestParam("cuocThiId") Long cuocThiId,
                                 RedirectAttributes redirectAttributes) {
        List<String[]> listFail = new ArrayList<>();
        try {
            listFail = phieuKetQuaService.importPhieuSuaDiemFromExcel(file);
            redirectAttributes.addFlashAttribute("message", "Kết quả đã được thêm vào.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi nhập: " + e.getMessage());
            return "redirect:/api/excel/import/form/pkq/cuocThiId/" + cuocThiId;
        }
        redirectAttributes.addFlashAttribute("listFail", listFail);
        return "redirect:/api/excel/import/form/pkq/cuocThiId/" + cuocThiId;
    }

    // ************************ Thực hiện chức năng nhập phiếu đăng ký bằng fiel excel *****************************
    @GetMapping("/import/form/pdk/cuocThiId/{id}")
    public String importFileExcelDPDK(@PathVariable Long id, Model model) {
        model.addAttribute("cuocThiId", id);
        return "Admin/PhieuDangKy/import-FileExcel";
    }

    // Lưu dữ liệu từ file excel vào database
    @PostMapping("/import/form/pdk")
    public String importDSPDK(@RequestParam("file") MultipartFile file,
                                 @RequestParam("cuocThiId") Long cuocThiId,
                                 RedirectAttributes redirectAttributes) {
        List<String[]> listFail = new ArrayList<>();
        try {
            CuocThi cuocThi = cuocThiService.getCuocThiById(cuocThiId).orElseThrow(() -> new EntityNotFoundException("CuocThi not found with id: " + cuocThiId));
            listFail = phieuKyService.importPhieuDangKyFromExcel(file, cuocThi);
            redirectAttributes.addFlashAttribute("message", "Danh sách phiếu đăng ký đã được thêm vào.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi nhập: " + e.getMessage());
            return "redirect:/api/excel/import/form/pdk/cuocThiId/" + cuocThiId;
        }

        for (String[] failRow : listFail) {
            for (int j = 0; j < failRow.length; j++) {
                if (j == 3) { // Cột index = 3
                    try {
                        double excelDate = Double.parseDouble(failRow[j]);
                        // Chuyển đổi từ số ngày Excel sang Date
                        LocalDate  date = LocalDate.of(1900, 1, 1).plusDays((long) excelDate - 2); // Trừ 2 ngày (1 cho Excel epoch + 1 do Excel sai sót)
                        failRow[j] = date.toString(); // Định dạng lại theo yyyy-MM-dd
                    } catch (NumberFormatException e) {
                        failRow[j] = "Invalid Date"; // Nếu không phải số
                    }
                }
            }
        }


        redirectAttributes.addFlashAttribute("listFail", listFail);
        return "redirect:/api/excel/import/form/pdk/cuocThiId/" + cuocThiId;
    }


    @GetMapping("/export/diem/cuocThi/{cuocThiId}/truong/{truongId}")
    public ResponseEntity<byte[]> exportDiemTheoTruong(
            @PathVariable Long cuocThiId,
            @PathVariable Long truongId) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách thí sinh");

        // Tạo tiêu đề
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã phiếu");
        headerRow.createCell(1).setCellValue("Cuộc thi");
        headerRow.createCell(2).setCellValue("CCCD");
        headerRow.createCell(3).setCellValue("Họ và tên");
        headerRow.createCell(4).setCellValue("Email");
        headerRow.createCell(5).setCellValue("SĐT");
        headerRow.createCell(6).setCellValue("Điểm");

        // Lấy dữ liệu
        List<PhieuKetQua> danhSach = phieuKetQuaService.getPhieuKetQuaTheoTruongVaCuocThi(truongId, cuocThiId);

        int rowNum = 1;
        for (PhieuKetQua ketQua : danhSach) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(ketQua.getId());
            row.createCell(1).setCellValue(ketQua.getPhieuDangKy().getCuocThi().getTenCuocThi());
            row.createCell(2).setCellValue(ketQua.getPhieuDangKy().getUser().getCccd());
            row.createCell(3).setCellValue(ketQua.getPhieuDangKy().getUser().getHoten());
            row.createCell(4).setCellValue(ketQua.getPhieuDangKy().getEmail());
            row.createCell(5).setCellValue(ketQua.getPhieuDangKy().getSdt());
            row.createCell(6).setCellValue(ketQua.getDiem());
        }

        // Xuất file Excel
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=danhSachThiSinh.xlsx");
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}