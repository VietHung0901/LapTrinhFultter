package DoAnCuoiKyJava.HeThongHoTroCuocThi.Constant;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public enum Role {
    ADMIN(1),
    USER(2),
    MANAGER(3),
    ADMIN_SCHOOL(4);
    public final long value;
}
