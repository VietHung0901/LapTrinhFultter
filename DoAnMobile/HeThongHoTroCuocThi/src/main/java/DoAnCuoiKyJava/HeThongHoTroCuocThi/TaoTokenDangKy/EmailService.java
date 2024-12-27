package DoAnCuoiKyJava.HeThongHoTroCuocThi.TaoTokenDangKy;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, User user, String confirmationUrl) {
        SimpleMailMessage message = new SimpleMailMessage();

        // Nội dung email tùy chỉnh
        String messageContent = "Xin chào " + user.getHoten() + ",\n\n"
                + "Cảm ơn bạn đã đăng ký tài khoản ở Hệ thống hỗ trợ cuộc thi của chúng tôi.\n"
                + "Vui lòng nhấp vào liên kết dưới đây để xác nhận email của bạn:\n\n"
                + confirmationUrl + "\n\n"
                + "Thông tin của bạn:\n"
                + "Email: " + user.getEmail() + "\n"
                + "SDT: " + user.getPhone() + "\n"
                + "Trường: " + user.getTruong().getTenTruong() + "\n"
                + "Ngày sinh: " + user.getNgaySinh() + "\n"
                + "Trân trọng,\nĐội ngũ hỗ trợ";

        message.setTo(to);
        message.setSubject(subject);
        message.setText(messageContent);
        mailSender.send(message);
    }

    public void sendEmailFail(String to, String subject, User user) {
        SimpleMailMessage message = new SimpleMailMessage();

        // Nội dung email tùy chỉnh
        String messageContent = "Xin chào " + user.getHoten() + ",\n\n"
                + "Cảm ơn bạn đã đăng ký tài khoản ở Hệ thống hỗ trợ cuộc thi của chúng tôi.\n"
                + "Rất tiếc thông tin cá nhân bạn đăng ký không khớp với thông tin cá nhân của bạn hoặc không hợp lê!!\n"
                + "Bạn có thể thử đăng ký lại với thông tin hợp lệ: \n"
                + "http://localhost:8080/register" + "\n\n"
                + "Trân trọng,\nĐội ngũ hỗ trợ";

        message.setTo(to);
        message.setSubject(subject);
        message.setText(messageContent);
        mailSender.send(message);
    }

    public void sendEmailSuccess(String to, String subject, User user) {
        SimpleMailMessage message = new SimpleMailMessage();

        // Nội dung email tùy chỉnh
        String messageContent = "Xin chào " + user.getHoten() + ",\n\n"
                + "Cảm ơn bạn đã đăng ký tài khoản ở Hệ thống hỗ trợ cuộc thi của chúng tôi.\n"
                + "Tài khoản của bạn đã được chúng tôi xác nhận hợp lệ\n"
                + "Bạn có thể đăng nhập tài đây: \n"
                + "http://localhost:8080/login" + "\n\n"
                + "Trân trọng,\nĐội ngũ hỗ trợ";

        message.setTo(to);
        message.setSubject(subject);
        message.setText(messageContent);
        mailSender.send(message);
    }

    public void sendEmailFogetPassword(String to, String subject, User user, String confirmationUrl) {
        SimpleMailMessage message = new SimpleMailMessage();

        // Nội dung email tùy chỉnh
        String messageContent = "Xin chào " + user.getHoten() + ",\n\n"
                + "Bạn đã yêu cầu đổi mật khẩu cho tài khoản Hệ thống hỗ trợ cuộc thi MOS.\n"
                + "Nhấp vào liên kết dưới đây để đổi mật khẩu:\n\n"
                + confirmationUrl + "\n\n"
                + "Nếu bạn không đưa ra yêu cầu thì có thể bỏ qua đoạn tin nhắn này.\n"
                + "Trân trọng,\nĐội ngũ hỗ trợ";

        message.setTo(to);
        message.setSubject(subject);
        message.setText(messageContent);
        mailSender.send(message);
    }

    /*--------------------------------- Đăng kí theo dạng trường ---------------------------------------*/
    public void sendEmailSuccessSchool(String to, String subject, User user) {
        SimpleMailMessage message = new SimpleMailMessage();

        String messageContent = "Xin chào " + user.getHoten() + ",\n\n"
                + "Cảm ơn bạn đã đăng ký tài khoản ở Hệ thống hỗ trợ cuộc thi của chúng tôi.\n"
                + "Tài khoản của bạn đã được chúng tôi xác nhận hợp lệ.\n\n"
                + "Bạn có thể đăng nhập tại đây: \n"
                + "http://localhost:8080/login\n\n"
                + "Thông tin đăng nhập của bạn là:\n"
                + "Username: " + user.getUsername() + "\n"
                + "Password: " + "Tên(không dấu) + 6 chữ số sau CCCD + @" + "\n"
                + "VD: Nguyễn Thị Huyền - 036093xxxxxx - @ -> Huyenxxxxxx@" + "\n\n"
                + "Nếu bạn muốn thay đổi mật khẩu, vui lòng sử dụng chức năng quên mật khẩu trên trang đăng nhập.\n\n"
                + "Trân trọng,\nĐội ngũ hỗ trợ";

        message.setTo(to);
        message.setSubject(subject);
        message.setText(messageContent);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEmailFailSchool(String to, List<String> duplicateInfoList, User user) {
        SimpleMailMessage message = new SimpleMailMessage();

        String duplicateInfo = String.join(", ", duplicateInfoList);

        String messageContent = "Xin chào " + user.getHoten() + ",\n\n"
                + "Chúng tôi rất tiếc thông báo rằng thông tin " + duplicateInfo + " của bạn đã tồn tại trong hệ thống của chúng tôi.\n"
                + "Vui lòng sử dụng thông tin khác hoặc liên hệ với chúng tôi để được hỗ trợ.\n\n"
                + "Trân trọng,\nĐội ngũ hỗ trợ";

        message.setTo(to);
        message.setSubject("Thông báo: Thông tin bị trùng lặp");
        message.setText(messageContent);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*---------------------- Thông báo trùng CCCD nhựng vẫn cập nhật thông tin -------------------------*/
    public void sendUpdateNotificationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(user.getEmail());
        message.setSubject("Thông báo: Cập nhật thông tin thành công");

        String content = "Xin chào " + user.getHoten() + ",\n\n"
                + "Thông tin của bạn đã được cập nhật thành công trong hệ thống.\n"
                + "Thông tin hiện tại:\n"
                + "Email: " + user.getEmail() + "\n"
                + "Số điện thoại: " + user.getPhone() + "\n"
                + "Mã trường: " + user.getTruong() + "\n\n"
                + "Trân trọng,\nĐội ngũ hỗ trợ";

        message.setText(content);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();  // Log lỗi nếu có sự cố khi gửi email
        }
    }
}

