class QuyDinh {
  final int id;
  final String tenQuyDinh;
  final String moTa;

  QuyDinh({
    required this.id,
    required this.tenQuyDinh,
    required this.moTa,
  });

  // Factory method to parse JSON into PhieuDangKy object
  factory QuyDinh.fromJson(Map<String, dynamic> json) {
    return QuyDinh(
      id: json['id'],
      tenQuyDinh: json['tenQuyDinh'],
      moTa: json['moTa'],
    );
  }
}
