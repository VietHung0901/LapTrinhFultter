package DoAnCuoiKyJava.HeThongHoTroCuocThi.Repositories;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
@Repository
public interface IUserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findById(Long id);
    void deleteById(Long id);
    Optional<User> findByCccd(String cccd);
    boolean existsByUsername(String username);
    boolean existsByCccd(String cccd);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    List<User> findUserByTrangThai(int trangThai);

    @Query("SELECT lt.tenLoaiTruong, COUNT(u.id) " +
            "FROM User u " +
            "JOIN u.truong t " +
            "JOIN t.loaiTruong lt " +
            "GROUP BY lt.tenLoaiTruong")
    List<Object[]> getUserCountsByLoaiTruong();
}
