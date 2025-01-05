class NoiDung {
  final int id;
  final String tenNoiDung;
  final String moTa;

  NoiDung({
    required this.id,
    required this.tenNoiDung,
    required this.moTa,
  });

  // Factory method to parse JSON into PhieuDangKy object
  factory NoiDung.fromJson(Map<String, dynamic> json) {
    return NoiDung(
      id: json['id'],
      tenNoiDung: json['tenNoiDung'],
      moTa: json['moTa'],
    );
  }
}
