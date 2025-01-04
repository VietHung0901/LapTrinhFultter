import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:mos/ApiService/Auth/AuthService.dart';
import 'package:mos/ApiService/HTTPService.dart';
import 'package:mos/Class/PhieuDangKy.dart';
import 'package:mos/Class/StringURL.dart';

class ResultPage extends StatefulWidget {
  final int cuocThiId;

  ResultPage({required this.cuocThiId});

  @override
  _ResultPageState createState() => _ResultPageState();
}

class _ResultPageState extends State<ResultPage> {
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
    final response = await httpService.get(
        StringURL().adminphieudangky + '/list?cuocThiId=${widget.cuocThiId}');

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
            const SnackBar(content: Text('Hết phiên đăng nhập, vui lòng đăng nhập lại!')),
          );
        } else if (response.statusCode == 403) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text('Bạn không có quyền truy cập tài nguyên này.')),
          );
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text('Lỗi không xác định: ${response.statusCode}')),
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
          'Danh Sách Kết Quả',
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
                        DataColumn(label: Text('STT')),
                        DataColumn(label: Text('Họ Tên')),
                        DataColumn(label: Text('Cuộc Thi')),
                        DataColumn(label: Text('CCCD')),
                        DataColumn(label: Text('Điểm')),
                      ],
                      rows: data.asMap().entries.map((entry) {
                        int index = entry.key;
                        PhieuDangKy item = entry.value;
                        return DataRow(
                          color: MaterialStateProperty.resolveWith<Color?>(
                            (Set<MaterialState> states) {
                              return index.isEven ? Colors.grey[200] : Colors.white;
                            },
                          ),
                          cells: [
                            DataCell(Text((index + 1).toString())), // STT
                            DataCell(Text(item.hoTen)), // Họ Tên
                            DataCell(Text(item.tenCuocThi)), // Cuộc Thi
                            DataCell(Text(item.cccd)), // CCCD
                            DataCell(Text(item.diem.toString())), // Điểm
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
