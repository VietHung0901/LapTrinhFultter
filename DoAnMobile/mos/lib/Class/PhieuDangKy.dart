class PhieuDangKy {
  final int id;
  final String tenCuocThi;
  final String cccd;
  final String hoTen;
  final int diem;

  PhieuDangKy({
    required this.id,
    required this.tenCuocThi,
    required this.cccd,
    required this.hoTen,
    required this.diem,
  });

  // Factory method to parse JSON into PhieuDangKy object
  factory PhieuDangKy.fromJson(Map<String, dynamic> json) {
    return PhieuDangKy(
      id: json['id'],
      tenCuocThi: json['tenCuocThi'],
      cccd: json['cccd'],
      hoTen: json['hoTen'],
      diem: json['diem'],
    );
  }
}
