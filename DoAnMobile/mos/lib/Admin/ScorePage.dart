import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:mos/ApiService/Auth/AuthService.dart';
import 'package:mos/ApiService/HTTPService.dart';
import 'package:mos/Class/StringURL.dart';

class ScorePage extends StatefulWidget {
  final int contestId;
  final List<Map<String, dynamic>> scoreTable;

  ScorePage({required this.scoreTable, required this.contestId});

  @override
  _ScorePageState createState() => _ScorePageState();
}

class _ScorePageState extends State<ScorePage> {
  late List<Map<String, dynamic>> scoreTable;
  final HTTPService httpService = HTTPService();
  AuthService authservice = AuthService();
  bool isAllCorrect = false; // Biến trạng thái để kiểm tra tất cả đều đúng
  bool bienXemDiem = false;

  @override
  void initState() {
    super.initState();
    scoreTable = widget.scoreTable; // Lưu scoreTable từ widget vào state
  }

// Hàm kiểm tra thông tin
  Future<void> kiemTraDanhSach() async {
    List<Map<String, dynamic>> jsonList = scoreTable;
    final response = await httpService.putList(
      StringURL().adminphieuketqua +
          '/kiem-tra-danh-sach?cuocThiId=${widget.contestId}', // Thêm cuocThiId vào URL
      jsonList, // Chuyển đổi danh sách phiếu kết quả thành chuỗi JSON
    );

    try {
      if (response.statusCode == 200) {
        final responseData = json.decode(response.body);

        if (responseData['status'] == 'success') {
          if (responseData['message'] != null) {
            // ScaffoldMessenger.of(context).showSnackBar(
            //   SnackBar(content: Text(responseData['message'])),
            // );
          }
          List<dynamic> responseBody = responseData['data'];
          for (int i = 0; i < responseBody.length; i++) {
            if (i < scoreTable.length) {
              setState(() {
                scoreTable[i]['trangThai'] =
                    responseBody[i]; // Cập nhật trangThai từ dữ liệu trả về
              });
            }
          }
          setState(() {
            isAllCorrect = scoreTable.every((entry) =>
                entry['trangThai'] == 1); // Kiểm tra tất cả trạng thái
                bienXemDiem = scoreTable.every((entry) =>
                entry['trangThai'] == 4); // Kiểm tra tất cả trạng thái
          });
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text(responseData['message'])),
          );
        }
      } else {
        if (response.statusCode == 401) {
          await authservice.logout(context);
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
                content: Text('Hết phiên đăng nhập, vui lòng đăng nhập lại!')),
          );
        } else if (response.statusCode == 403)
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
                content: Text('Bạn không có quyền truy cập tài nguyên này.')),
          );
        else
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
                content: Text('Lỗi không xác định: ${response.statusCode}')),
          );
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Lỗi: $e')),
      );
    }
  }

  // Hàm nhập điểm
  Future<void> nhapDanhSachPhieuKetQua() async {
    List<Map<String, dynamic>> jsonList = scoreTable;

    final response = await httpService.postList(
      StringURL().adminphieuketqua +
          '/nhap-danh-sach', // Thêm cuocThiId vào URL
      jsonList, // Chuyển đổi danh sách phiếu kết quả thành chuỗi JSON
    );

    try {
      if (response.statusCode == 200) {
        // dùng utf8 để có thể giải mã tiếng Việt
        final decodedResponse = utf8.decode(response.bodyBytes);
        final responseData = json.decode(decodedResponse);
        // final responseData = json.decode(response.body);

        if (responseData['status'] == 'success') {
          if (responseData['message'] != null) {
            // ScaffoldMessenger.of(context).showSnackBar(
            //   SnackBar(content: Text(responseData['message'])),
            // );
            kiemTraDanhSach();
          }
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text(responseData['message'])),
          );
        }
      } else {
        if (response.statusCode == 401) {
          await authservice.logout(context);
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
                content: Text('Hết phiên đăng nhập, vui lòng đăng nhập lại!')),
          );
        } else if (response.statusCode == 403)
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
                content: Text('Bạn không có quyền truy cập tài nguyên này.')),
          );
        else
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
                content: Text('Lỗi không xác định: ${response.statusCode}')),
          );
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Lỗi: $e')),
      );
    }
  }

  // Hàm để xác định màu sắc cho trạng thái
  Color _getTrangThaiColor(int trangThai) {
    if (trangThai == 0) {
      return Colors.amber; // Vàng nếu chưa kiểm tra
    } else if (trangThai == 1) {
      return Colors.green; // Xanh lá nếu chính xác
    } else if (trangThai == 2) {
      return Colors.red; // Đỏ nếu sai thông tin
    } else if (trangThai == 4) {
      return Colors.blue; // Đỏ nếu sai thông tin
    } else {
      return Colors.black; // Màu đen nếu không xác định được trạng thái
    }
  }

  String _getTrangThai(int trangThai) {
    if (trangThai == 0) {
      return 'Chưa Kiểm Tra';
    } else if (trangThai == 1) {
      return 'Chính Xác';
    } else if (trangThai == 2) {
      return 'Sai Thông Tin';
    } else if (trangThai == 4) {
      return 'Đã có điểm';
    } else {
      return 'Không Xác Định'; // Trường hợp không có giá trị hợp lệ
    }
  }

  void _xoaDoiTuong(String maPhieu) {
    setState(() {
      // Xóa đối tượng có maPhieu tương ứng khỏi scoreTable
      scoreTable.removeWhere((entry) => entry['maPhieu'] == maPhieu);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Bảng Điểm Quét Được'),
        backgroundColor: Colors.blueAccent,
      ),
      body: OrientationBuilder(
        builder: (context, orientation) {
          return SingleChildScrollView(
            // Cho phép cuộn cả chiều dọc và chiều ngang
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                children: [
                  SingleChildScrollView(
                    // Cuộn ngang cho bảng
                    scrollDirection: Axis.horizontal,
                    child: DataTable(
                      columnSpacing: 20,
                      headingTextStyle: TextStyle(
                        fontWeight: FontWeight.bold,
                        color: Colors.blueAccent,
                        fontSize: 16,
                      ),
                      dataTextStyle: TextStyle(fontSize: 14),
                      columns: const [
                        DataColumn(
                          label: Text('Mã',
                              style: TextStyle(fontWeight: FontWeight.bold)),
                        ),
                        DataColumn(
                          label: Text('CCCD',
                              style: TextStyle(fontWeight: FontWeight.bold)),
                        ),
                        DataColumn(
                          label: Text('Họ Tên',
                              style: TextStyle(fontWeight: FontWeight.bold)),
                        ),
                        DataColumn(
                          label: Text('Thời Gian',
                              style: TextStyle(fontWeight: FontWeight.bold)),
                        ),
                        DataColumn(
                          label: Text('Điểm',
                              style: TextStyle(fontWeight: FontWeight.bold)),
                        ),
                        DataColumn(
                          label: Text('Trạng Thái',
                              style: TextStyle(fontWeight: FontWeight.bold)),
                        ),
                        DataColumn(
                          label: Text('Xóa',
                              style: TextStyle(fontWeight: FontWeight.bold)),
                        ),
                      ],
                      rows: scoreTable.map((entry) {
                        int trangThai = entry['trangThai'] ?? -1;
                        return DataRow(
                          cells: [
                            DataCell(Text(entry['maPhieu'] ?? '')),
                            DataCell(Text(entry['cccd'] ?? '')),
                            DataCell(Text(entry['hoTen'] ?? '')),
                            DataCell(Text(
                              '${entry['phut']?.toString().padLeft(2, '0') ?? '00'}:${entry['giay']?.toString().padLeft(2, '0') ?? '00'}',
                            )),
                            DataCell(Text(
                              entry['diem']?.toString() ?? '0',
                              style: TextStyle(
                                  color: Colors.green,
                                  fontWeight: FontWeight.w500),
                            )),
                            DataCell(
                              Text(
                                _getTrangThai(trangThai),
                                style: TextStyle(
                                  color: _getTrangThaiColor(trangThai),
                                ),
                              ),
                            ),
                            // Cột Xóa
                            DataCell(
                              IconButton(
                                icon: Icon(Icons.delete, color: Colors.red),
                                onPressed: () {
                                  _xoaDoiTuong(entry['maPhieu']); // Gọi hàm xóa
                                },
                              ),
                            ),
                          ],
                        );
                      }).toList(),
                    ),
                  ),
                  SizedBox(height: 20), // Khoảng cách giữa bảng và nút
                  ElevatedButton(
                    onPressed: () {
                      kiemTraDanhSach();
                    },
                    child: Text('Kiểm Tra Thông Tin'),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.blueAccent,
                      padding:
                          EdgeInsets.symmetric(vertical: 12, horizontal: 24),
                    ),
                  ),
                  // Nếu tất cả trạng thái đều là 1, hiển thị thêm nút nhập điểm
                  if (isAllCorrect)
                    ElevatedButton(
                      onPressed: () {
                        // Xử lý nhập điểm ở đây
                        nhapDanhSachPhieuKetQua();
                      },
                      child: Text('Nhập Điểm'),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.green,
                        padding:
                            EdgeInsets.symmetric(vertical: 12, horizontal: 24),
                      ),
                    ),
                  // Nếu tất cả trạng thái đều là 4, hiển thị thêm nút Xem bảng điểm
                  if (bienXemDiem)
                    ElevatedButton(
                      onPressed: () {
                        // chuyển tới trang xem bảng điểm
                      },
                      child: Text('Xem bảng điểm'),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.green,
                        padding:
                            EdgeInsets.symmetric(vertical: 12, horizontal: 24),
                      ),
                    ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}
