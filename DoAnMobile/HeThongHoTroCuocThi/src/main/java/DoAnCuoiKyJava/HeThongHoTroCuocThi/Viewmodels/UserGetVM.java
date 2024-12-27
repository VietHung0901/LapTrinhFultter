package DoAnCuoiKyJava.HeThongHoTroCuocThi.Viewmodels;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
@Builder
public record UserGetVM(Long id, String hoten, String truongName, String email, String phone, String imageUrl, Long truongId) {
    public static UserGetVM from(@NotNull User user) {
        return UserGetVM.builder()
                .id(user.getId())
                .hoten(user.getHoten())
                .email(user.getEmail())
                .phone(user.getPhone())
                .imageUrl(user.getImageUrl())
                .truongId(user.getTruong().getId())
                .truongName(user.getTruong().getTenTruong())
                .build();
    }
}