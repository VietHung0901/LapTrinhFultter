import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:mos/ApiService/Auth/AuthService.dart';
import 'package:mos/ApiService/HTTPService.dart';
import 'package:mos/Class/PhieuDangKy.dart';
import 'package:mos/Class/StringURL.dart';
import 'package:mos/User/RankingPage.dart';

class PhieuDangKyPage extends StatefulWidget {
  
  @override
  _PhieuDangKyPageState createState() => _PhieuDangKyPageState();
}

class _PhieuDangKyPageState extends State<PhieuDangKyPage> {
  List<PhieuDangKy> data = [];
  bool _isLoading = true;
  final HTTPService httpService = HTTPService();
  AuthService authservice = AuthService();

  @override
  void initState() {
    super.initState();
    fetchData();
  }

  Future<void> fetchData() async {
    final response =
        await httpService.get(StringURL().userphieudangky + '/list');

    try {
      if (response.statusCode == 200) {
        // dùng utf8 để có thể giải mã tiếng Việt
        final decodedResponse = utf8.decode(response.bodyBytes);
        final responseData = json.decode(decodedResponse);
        // final responseData = json.decode(response.body);
        if (responseData['status'] == 'success') {
          setState(() {
            final List<dynamic> listApi = responseData['data'];
            data = listApi.map((json) => PhieuDangKy.fromJson(json)).toList();
            _isLoading = false;
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
        } else if (response.statusCode == 403) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
                content: Text('Bạn không có quyền truy cập tài nguyên này.')),
          );
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
                content: Text('Lỗi không xác định: ${response.statusCode}')),
          );
        }
      }
    } catch (e) {
      setState(() {
        _isLoading = false;
      });
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Lỗi: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          'Danh Sách Đăng ký',
          style: TextStyle(fontWeight: FontWeight.bold),
        ),
        backgroundColor: Colors.blueAccent,
      ),
      body: _isLoading
          ? Center(child: CircularProgressIndicator())
          : Padding(
              padding: const EdgeInsets.all(16.0),
              child: Card(
                elevation: 5,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8),
                ),
                child: SingleChildScrollView(
                  scrollDirection: Axis.vertical,
                  child: SingleChildScrollView(
                    scrollDirection: Axis.horizontal,
                    child: DataTable(
                      columnSpacing: 20,
                      headingRowColor: MaterialStateColor.resolveWith(
                        (states) => Colors.blueAccent,
                      ),
                      headingTextStyle: TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                      ),
                      columns: [
                        DataColumn(label: Text('Mã')),
                        DataColumn(label: Text('Họ Tên')),
                        DataColumn(label: Text('Cuộc Thi')),
                        DataColumn(label: Text('CCCD')),
                        DataColumn(label: Text('Kết quả')),
                        DataColumn(label: Text('Hành động')),
                      ],
                      rows: data.asMap().entries.map((entry) {
                        int index = entry.key;
                        PhieuDangKy item = entry.value;
                        return DataRow(
                          color: WidgetStateProperty.resolveWith<Color?>(
                            (Set<WidgetState> states) {
                              return index.isEven
                                  ? Colors.grey[200]
                                  : Colors.white;
                            },
                          ),
                          cells: [
                            DataCell(Text((item.id).toString())), // STT
                            DataCell(Text(item.hoTen)), // Họ Tên
                            DataCell(Text(item.tenCuocThi)), // Cuộc Thi
                            DataCell(Text(item.cccd)), // CCCD
                            DataCell(
                              Text(
                                item.diem, // Nội dung hiển thị
                                style: TextStyle(
                                  color: item.diem == "Chưa có"
                                      ? Colors.orangeAccent
                                      : Colors.green, // Điều kiện màu
                                ),
                              ),
                            ),
                            DataCell(
                              TextButton(
                                onPressed: () {
                                  // Hành động khi nhấn nút
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                      builder: (context) => RankingPage(cuocThiId: item.cuocThiId),
                                    ),
                                  );
                                },
                                child: Text(
                                  'Xem',
                                  style: TextStyle(color: Colors.blue),
                                ),
                              ),
                            ),
                          ],
                        );
                      }).toList(),
                    ),
                  ),
                ),
              ),
            ),
    );
  }
}
