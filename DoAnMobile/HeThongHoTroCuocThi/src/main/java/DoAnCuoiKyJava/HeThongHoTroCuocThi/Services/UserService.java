package DoAnCuoiKyJava.HeThongHoTroCuocThi.Services;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Constant.Provider;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Constant.Role;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.Truong;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IRoleRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.ITruongRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories.IUserRepository;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Request.UserCreateRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Request.UserUpdateRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.TaoTokenDangKy.EmailService;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.TaoTokenDangKy.VerificationToken;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.TaoTokenDangKy.VerificationTokenRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
//Các thư viện dùng lưu ảnh vào local
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;


@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ITruongRepository truongRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = {Exception.class, Throwable.class})
    public void Save(@NotNull User user) {
        user.setPassword(new BCryptPasswordEncoder()
                .encode(user.getPassword()));
        userRepository.save(user);
    }

    public User findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public User findById(Long id) throws UsernameNotFoundException {
        return userRepository.findById(id);
    }

    public User findByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByCCCD(String id) {
        return userRepository.findByCccd(id);
    }

    public void createNewUser(UserCreateRequest userRequest) {
        String image = saveImage(userRequest.getImageUrl());
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setHoten(userRequest.getHoten());
        user.setCccd(userRequest.getCccd());
        user.setPassword(userRequest.getPassword());
        user.setPhone(userRequest.getPhone());
        user.setEmail(userRequest.getEmail());
        user.setGender(userRequest.getGender());
        user.setImageUrl(image);
        user.setTruong(userRequest.getTruong());
        user.setTrangThai(0);
        Save(user);
        setDefaultRole(user.getUsername());

        //Tạo token khi đăng ký
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1)); // Token hết hạn sau 1 ngày
        verificationTokenRepository.save(verificationToken);

        // Gửi email xác nhận
        String confirmationUrl = "http://localhost:8080/confirm?token=" + verificationToken.getToken();
        emailService.sendEmail(user.getEmail(), "Xác nhận email", user, confirmationUrl);
    }

    //Lưu quyền khi đăng nhập bằng tài khoản google
    public void saveOauthUser(String email, @NotNull String username) {
        if(userRepository.findByUsername(username) != null)
            return;
        var user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(username));
        user.setProvider(Provider.GOOGLE.value);
        user.getRoles().add(roleRepository.findRoleById(Role.USER.value));
        userRepository.save(user);
    }

    //Lưu quyền khi đăng ký thông thường s
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {Exception.class, Throwable.class})
    public void setDefaultRole(String username){
        userRepository.findByUsername(username)
                .getRoles()
                .add(roleRepository.findRoleById(Role.USER.value));
    }

    //Lưu quyền đăng kí thông qua nhà trường
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {Exception.class, Throwable.class})
    public void setDefaultRoleForSchoolRegistration(String cccd) {
        Optional<User> optionalUser = userRepository.findByCccd(cccd);

        optionalUser.ifPresentOrElse(user -> {
            user.getRoles().add(roleRepository.findRoleById(Role.USER.value));
            userRepository.save(user);
        }, () -> {
            System.out.println("Không tìm thấy người dùng với CCCD: " + cccd);
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    public String saveImage(MultipartFile file) {
        // Lấy tên file
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Đường dẫn lưu file
        /*String uploadDir = "/Users/tranviethung/Documents/Học tập/DACN_HeThongMOS/HeThongHoTroCuocThi/src/main/resources/static/images/";*/
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

    public void saveUser(UserUpdateRequest updatedUser) {
        User user = findById(updatedUser.getId());
        if (user != null) {
            user.setCccd(updatedUser.getCccd());
            user.setHoten(updatedUser.getHoten());
            user.setEmail(updatedUser.getEmail());
            user.setPhone(updatedUser.getPhone());
            user.setNgaySinh(updatedUser.getNgaySinh());
            if(user.getTrangThai() != 1)
                user.setTrangThai(0);
            String fileName = updatedUser.getImageUrl().getOriginalFilename();
            if(fileName != "") {
                String images = saveImage(updatedUser.getImageUrl());
                user.setImageUrl(images);
            }
            user.setTruong(updatedUser.getTruong());
            user.setGender(updatedUser.getGender());
            userRepository.save(user);
        }
    }

    public int getTotalUsers() {
        return userRepository.findAll().size(); // Đếm số lượng người dùng
    }

    // Example method to get user counts by LoaiTruong
    public List<Object[]> getUserCountsByLoaiTruong() {
        return userRepository.getUserCountsByLoaiTruong();
    }

    public List<User> getAllUsersByTrangThai(int trangThai) {
        return userRepository.findUserByTrangThai(trangThai);
    }

    public String checkUser(UserCreateRequest userRequest) {
        String fileName = userRequest.getImageUrl().getOriginalFilename();
        if(fileName.isEmpty()) {
            return "Vui lòng chọn ảnh cho tài khoản!";
        }
        if(userRepository.existsByCccd(userRequest.getCccd())){
            return "CCCD này đã được dùng cho tài khoản khác.";
        }
        if(userRepository.existsByPhone(userRequest.getPhone())) {
            return "SĐT này đã được dùng cho tài khoản khác.";
        }
        if(userRepository.existsByEmail(userRequest.getEmail())) {
            return "Email này đã được dùng cho tài khoản khác.";
        }
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            return "Tên người dùng đã tồn tại. Vui lòng chọn tên khác.";
        }
        return "success";
    }

    public void KhongDuyet(Long id) {
        User user = userRepository.findById(id);
        // Gửi email xác nhận tạo tài khoản thất bại
        emailService.sendEmailFail(user.getEmail(), "Xác nhận tài khoản MOS thất bại", user);
        user.setTrangThai(2);
        userRepository.save(user);
    }

    public void Duyet(Long id) {
        User user = userRepository.findById(id);
        // Gửi email xác nhận tạo tài khoản thành công
        emailService.sendEmailSuccess(user.getEmail(), "Xác nhận tài khoản MOS thành công", user);
        user.setTrangThai(1);
        userRepository.save(user);
    }

    public void sendEmailForgotPassword(User user) {
        //Tạo token khi đăng ký
        VerificationToken verificationToken = verificationTokenRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("User không hợp lê."));
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1)); // Token hết hạn sau 1 ngày
        verificationTokenRepository.save(verificationToken);

        // Gửi email xác nhận
        String confirmationUrl = "http://localhost:8080/confirmForgotPassword?token=" + verificationToken.getToken()
                                                                            + "&username=" + user.getUsername();
        emailService.sendEmailFogetPassword(user.getEmail(), "Đổi mật khẩu", user, confirmationUrl);
    }

    /*--------------------------------------- Admin-Truong UserSerivce -----------------------------------------------*/
    public void importStudentsFromExcel(MultipartFile file, List<User> successfulUsers, List<User> failedUsers) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            var sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    User user = parseUserFromRow(row, dataFormatter);
                    if (user != null && !isDuplicate(user)) {
                        userRepository.save(user);
                        setDefaultRoleForSchoolRegistration(user.getCccd());
                        successfulUsers.add(user);

                        VerificationToken verificationToken = new VerificationToken();
                        verificationToken.setToken(UUID.randomUUID().toString());
                        verificationToken.setUser(user);
                        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
                        verificationTokenRepository.save(verificationToken);

                    } else {
                        failedUsers.add(user);
                    }
                }
            }
        }
    }

    private boolean isDuplicate(User user) {
        List<String> duplicateInfoList = new ArrayList<>();

        if (userRepository.existsByCccd(user.getCccd())) {
            duplicateInfoList.add("Trùng CCCD");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            duplicateInfoList.add("Trùng Email");
        }
        if (userRepository.existsByPhone(user.getPhone())) {
            duplicateInfoList.add("Trùng SDT");
        }
        if (!duplicateInfoList.isEmpty()) {
            return true;
        }
        return false;
    }

    private User parseUserFromRow(Row row, DataFormatter dataFormatter) {
        String cccd = dataFormatter.formatCellValue(row.getCell(0));
        String email = dataFormatter.formatCellValue(row.getCell(1));
        String hoten = dataFormatter.formatCellValue(row.getCell(2));
        String phone = dataFormatter.formatCellValue(row.getCell(3));
        String tenTruongFromExcel = dataFormatter.formatCellValue(row.getCell(4));
        LocalDate birthDate = parseBirthDate(row.getCell(5));
        Integer gioiTinh = parseGender(dataFormatter.formatCellValue(row.getCell(6)));

        String username = extractUsernameFromEmail(email);

        String password = new BCryptPasswordEncoder().encode(generateCustomPassword(hoten, cccd));

        if (username == null) {
            return null;
        }

        return createUser(cccd, email, hoten, password, phone, username, birthDate, tenTruongFromExcel, gioiTinh);
    }

    private LocalDate parseBirthDate(Cell cell) {
        if (cell != null) {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            } else if (cell.getCellType() == CellType.STRING) {
                String birthDateString = cell.getStringCellValue().trim();
                try {
                    return LocalDate.parse(birthDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (DateTimeParseException e) {
                    System.out.println("Định dạng ngày không hợp lệ: " + birthDateString);
                }
            }
        }
        return null;
    }

    private String extractUsernameFromEmail(String email) {
        if (email != null && email.contains("@")) {
            return email.split("@")[0];
        }
        System.out.println("Email không hợp lệ: " + email);
        return null;
    }

    private User createUser(String cccd, String email, String hoten, String password, String phone,
                            String username, LocalDate birthDate, String tenTruongFromExcel, Integer gioiTinh) {
        User user = new User();
        user.setCccd(cccd);
        user.setEmail(email);
        user.setHoten(hoten);
        user.setPassword(password);
        user.setPhone(phone);
        user.setUsername(username);
        user.setNgaySinh(birthDate);
        user.setGender(gioiTinh);

        Truong truong = truongRepository.findByTenTruong(tenTruongFromExcel);
        if (truong != null) {
            user.setTruong(truong);
        } else {
            System.out.println("Không tìm thấy trường với tên: " + tenTruongFromExcel);
        }
        return user;
    }

    public String generateCustomPassword(String hoten, String cccd) {
        String[] nameParts = hoten.trim().split("\\s+");
        String lastName = nameParts[nameParts.length - 1];
        String lastNameNoAccent = removeVietnameseAccent(lastName);
        String cccdSuffix = cccd.substring(Math.max(0, cccd.length() - 6));
        return lastNameNoAccent + cccdSuffix + "@";
    }

    private String removeVietnameseAccent(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").replaceAll("đ", "d").replaceAll("Đ", "D");
    }

    private Integer parseGender(String genderValue) {
        if ("Nam".equalsIgnoreCase(genderValue)) {
            return 0;  // 1 là Nam
        } else if ("Nữ".equalsIgnoreCase(genderValue)) {
            return 1;  // 0 là Nữ
        }
        return null; // Trả về null nếu giá trị không hợp lệ
    }


}