import 'dart:convert';

import 'package:flutter/material.dart';
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
  bool isAllCorrect = false; // Biến trạng thái để kiểm tra tất cả đều đúng

  @override
  void initState() {
    super.initState();
    scoreTable = widget.scoreTable; // Lưu scoreTable từ widget vào state
  }

// Hàm kiểm tra thông tin
  Future<List<Map<String, dynamic>>> kiemTraDanhSach() async {
    try {
      // Tạo danh sách các phiếu kết quả dưới dạng JSON
      List<Map<String, dynamic>> jsonList = scoreTable;

      // Gửi yêu cầu PUT đến API
      final response = await httpService.putList(
        StringURL().adminphieuketqua + '/kiem-tra-danh-sach?cuocThiId=${widget.contestId}', // Thêm cuocThiId vào URL
        jsonList, // Chuyển đổi danh sách phiếu kết quả thành chuỗi JSON
      );

      // Kiểm tra xem yêu cầu có thành công không (mã trạng thái HTTP 200)
      if (response.statusCode == 200) {
        // Nếu thành công, trả về danh sách kết quả kiểm tra từ API
        List<dynamic> responseBody = jsonDecode(response.body);

        // Cập nhật trạng thái cho mỗi phiếu kết quả trong scoreTable
        for (int i = 0; i < responseBody.length; i++) {
          if (i < scoreTable.length) {
            setState(() {
              scoreTable[i]['trangThai'] =
                  responseBody[i]; // Cập nhật trangThai từ dữ liệu trả về
            });
          }
        }

        // Kiểm tra xem tất cả các trạng thái có phải là 1 (Chính Xác) không
        setState(() {
          isAllCorrect = scoreTable.every(
              (entry) => entry['trangThai'] == 1); // Kiểm tra tất cả trạng thái
        });

        return scoreTable;
      } else {
        // Nếu không thành công, ném lỗi
        throw Exception('Lỗi khi gọi API: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Có lỗi xảy ra: $e');
    }
  }
  
  // Hàm nhập điểm
  Future<String> nhapDanhSachPhieuKetQua() async {
    List<Map<String, dynamic>> jsonList = scoreTable;

    // Gửi yêu cầu POST đến API
    try {
      final response = await httpService.postList(
        StringURL().adminphieuketqua + '/nhap-danh-sach', // Thêm cuocThiId vào URL
        jsonList, // Chuyển đổi danh sách phiếu kết quả thành chuỗi JSON
      );
      if (response.statusCode == 200) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text(response.body)),
        );
        return response.body; // Trả về thông báo từ server
      } else {
        throw Exception('Lỗi: ${response.statusCode} - ${response.body}');
      }
    } catch (e) {
      throw Exception('Lỗi khi gọi API: $e');
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
    } else {
      return 'Không Xác Định'; // Trường hợp không có giá trị hợp lệ
    }
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
                      columnSpacing: 20, // Tăng khoảng cách giữa các cột
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
                      ],
                      rows: scoreTable.map((entry) {
                        int trangThai = entry['trangThai'] ??
                            -1; // Lấy trang thái hoặc gán -1 nếu không có
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
                                  color: _getTrangThaiColor(
                                      trangThai), // Đổi màu dựa trên trạng thái
                                ),
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
                      kiemTraDanhSach().then((_) {
                        // Sau khi kiểm tra thành công, refresh lại giao diện
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(content: Text('Đã kiểm tra bảng điểm')),
                        );
                      }).catchError((e) {
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(content: Text('Lỗi: $e')),
                        );
                      });
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
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}
