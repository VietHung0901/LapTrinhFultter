package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.AdminSchoolControllers;


import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.Truong;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.ITruongRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IUserRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ADMIN_SCHOOL/import-students")
@RequiredArgsConstructor
public class AdminSchoolController {
    private final UserService userService;
    private final IUserRepository userRepository;
    private final ITruongRepository truongRepository;

    @GetMapping
    public String showUploadForm() {
        return "ADMIN_SCHOOL/import-students";
    }

    @PostMapping
    public String importStudents(@RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes, Model model) {
        List<User> successfulUsers = new ArrayList<>();

        List<User> failedUsers = new ArrayList<>();

        try {
            userService.importStudentsFromExcel(file, successfulUsers, failedUsers);

            redirectAttributes.addFlashAttribute("message", "Đăng ký thành công. Xin cảm ơn!");

            if (!failedUsers.isEmpty()) {
                model.addAttribute("failedUsers", failedUsers);
                return "ADMIN_SCHOOL/import-students";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi nhập: " + e.getMessage());
            return "redirect:/ADMIN_SCHOOL/import-students";
        }

        return "redirect:/ADMIN_SCHOOL/import-students";
    }

    @GetMapping("/update/{cccd}/{email}/{sdt}/{truongID}")
    public String showEditForm(@PathVariable String cccd,
                               @PathVariable String email,
                               @PathVariable String sdt,
                               @PathVariable Long truongID,
                               Model model) {

        User user1 = userService.getUserByCCCD(cccd)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thí sinh với CCCD: " + cccd));
        Truong truong = truongRepository.findTruongById(truongID);
        User user = new User();
        user.setCccd(cccd);
        user.setEmail(email);
        user.setPhone(sdt);
        user.setTruong(truong);
        user.setHoten(user1.getHoten());
        if (user1.getNgaySinh() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = user1.getNgaySinh().format(formatter);
            user.setNgaySinh(LocalDate.parse(formattedDate, formatter));
        }
        user.setGender(user1.getGender());

        model.addAttribute("user1", user1);

        model.addAttribute("user", user);

        return "ADMIN_SCHOOL/update-student";
    }

    @PostMapping("/update/{cccd}/{email}/{sdt}/{truongID}")
    public String updateStudent(@PathVariable String cccd,
                                @PathVariable String email,
                                @PathVariable String sdt,
                                @PathVariable Long truongID,
//                                @ModelAttribute("existingUser") User updatedUser,
                                RedirectAttributes redirectAttributes) {

        List<User> failedUsers = new ArrayList<>();

        try {

            User existingUser = userRepository.findByCccd(cccd)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thí sinh với CCCD: " + cccd));
            Truong truong = truongRepository.findTruongById(truongID);

            /*existingUser.setCccd(updatedUser.getCccd());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setTruong(truong);*/

            existingUser.setCccd(cccd);
            existingUser.setEmail(email);
            existingUser.setPhone(sdt);
            existingUser.setTruong(truong);

            userRepository.save(existingUser);

            redirectAttributes.addFlashAttribute("message", "Thông tin thí sinh đã được cập nhật thành công!");
            return "redirect:/ADMIN_SCHOOL/import-students";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi cập nhật: " + e.getMessage());
            return "redirect:/ADMIN_SCHOOL/import-students";
        }
    }
}



