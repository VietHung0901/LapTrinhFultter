class PhieuKetQua {
  String? maPhieu; // Mã phiếu
  String? cccd; // CCCD
  String? hoTen; // Họ và tên
  int? phut; // Phút
  int? giay; // Giây
  int? diem; // Điểm 
  int? trangThai;

  // Constructor
  PhieuKetQua({
    this.maPhieu,
    this.cccd,
    this.hoTen,
    this.phut,
    this.giay,
    this.diem,
  });

  // Phương thức để in thông tin của sinh viên
  @override
  String toString() {
    return 'Mã phiếu: $maPhieu, CCCD: $cccd, Họ tên: $hoTen, Thời gian: $phut phút $giay giây, Điểm: $diem';
  }

  factory PhieuKetQua.fromJson(Map<String, dynamic> json) {
    return PhieuKetQua(
      maPhieu: json['maPhieu'],
      cccd: json['cccd'],
      hoTen: json['hoTen'],
      phut: json['phut'],
      giay: json['giay'],
      diem: json['diem'],
    );
  }
}
