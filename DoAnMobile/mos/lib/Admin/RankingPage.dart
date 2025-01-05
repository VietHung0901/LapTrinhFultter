import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:mos/ApiService/Auth/AuthService.dart';
import 'package:mos/ApiService/HTTPService.dart';
import 'package:mos/Class/PhieuKetQua.dart';
import 'package:mos/Class/StringURL.dart';

class RankingPage extends StatefulWidget {
  final int cuocThiId;

  RankingPage({required this.cuocThiId});
  @override
  _RankingPageState createState() => _RankingPageState();
}

class _RankingPageState extends State<RankingPage> {
  List<PhieuKetQua> _danhSachKetQua = [];
  bool _isLoading = true;
  HTTPService httpService = HTTPService();
  AuthService authservice = AuthService();
  // Hàm gọi API
  Future<void> _fetchRanking() async {
    final response = await httpService.get(StringURL().adminphieuketqua + '/xem-bang-ket-qua?cuocThiId=${widget.cuocThiId}');
    
    try {
      if (response.statusCode == 200) {
        // dùng utf8 để có thể giải mã tiếng Việt
        final decodedResponse = utf8.decode(response.bodyBytes);
        final responseData = json.decode(decodedResponse);
        // final responseData = json.decode(response.body);
        if (responseData['status'] == 'success') {
          setState(() {
          final List<dynamic> listApi = responseData['data'];
            _danhSachKetQua = listApi.map((json) => PhieuKetQua.fromJson(json)).toList();
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
  void initState() {
    super.initState();
    _fetchRanking(); // Gọi API khi màn hình được khởi tạo
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Danh Sách Kết Quả'),
        backgroundColor: Colors.blueAccent,
      ),
      body: _isLoading
          ? Center(child: CircularProgressIndicator())
          : _danhSachKetQua.isEmpty
              ? Center(child: Text("Không có dữ liệu"))
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
                          headingRowColor: WidgetStateProperty.resolveWith(
                            (states) => Colors.blueAccent,
                          ),
                          headingTextStyle: TextStyle(
                            color: Colors.white,
                            fontWeight: FontWeight.bold,
                          ),
                          columns: [
                            DataColumn(label: Text('Hạng')),
                            DataColumn(label: Text('Họ Tên')),
                            DataColumn(label: Text('CCCD')),
                            DataColumn(label: Text('Điểm')),
                            DataColumn(label: Text('Thời Gian')),
                          ],
                          rows: _danhSachKetQua
                              .asMap()
                              .entries
                              .map((entry) {
                                int index = entry.key;
                                PhieuKetQua item = entry.value;
                                return DataRow(
                                  cells: [
                                    DataCell(Text((index + 1).toString())), // Hạng
                                    DataCell(Text(item.hoTen ?? "")), // Họ Tên
                                    DataCell(Text(item.cccd ?? "")), // CCCD
                                    DataCell(Text(item.diem.toString())), // Điểm
                                    DataCell(Text(
                                        "${item.phut} phút ${item.giay} giây")), // Thời gian
                                  ],
                                );
                              })
                              .toList(),
                        ),
                      ),
                    ),
                  ),
                ),
    );
  }
}