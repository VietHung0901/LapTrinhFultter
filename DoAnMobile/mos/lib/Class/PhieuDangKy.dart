class PhieuDangKy {
  final int id;
  final String tenCuocThi;
  final int cuocThiId;
  final String cccd;
  final String hoTen;
  final String diem;

  PhieuDangKy({
    required this.id,
    required this.tenCuocThi,
    required this.cuocThiId,
    required this.cccd,
    required this.hoTen,
    required this.diem,
  });

  // Factory method to parse JSON into PhieuDangKy object
  factory PhieuDangKy.fromJson(Map<String, dynamic> json) {
    return PhieuDangKy(
      id: json['id'],
      tenCuocThi: json['tenCuocThi'],
      cuocThiId: json['cuocThiId'],
      cccd: json['cccd'],
      hoTen: json['hoTen'],
      diem: json['diem'],
    );
  }
}
