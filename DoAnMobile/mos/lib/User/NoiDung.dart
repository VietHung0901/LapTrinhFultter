import 'dart:convert'; // Để xử lý JSON
import 'package:flutter/material.dart';
import 'package:mos/ApiService/Auth/AuthService.dart';
import 'package:mos/ApiService/HTTPService.dart';
import 'package:mos/Class/NoiDungRequest.dart';
import 'package:mos/Class/StringURL.dart'; // Thư viện HTTP

class NoiDungApiPage extends StatefulWidget {
  final int contestId;

  NoiDungApiPage({required this.contestId});

  @override
  _NoiDungApiPageState createState() => _NoiDungApiPageState();
}

class _NoiDungApiPageState extends State<NoiDungApiPage> {
  List<NoiDung> _noiDungList = [];
  bool _isLoading = true;
  final HTTPService httpService = HTTPService();
  AuthService authservice = AuthService();

  @override
  void initState() {
    super.initState();
    fetchNoiDungList();
  }

  Future<void> fetchNoiDungList() async {
    try {
      // Gọi API
      final response = await httpService
          .get(StringURL().usernoidung + '/list/${widget.contestId}');

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
          }
          setState(() {
            final List<dynamic> listApi = responseData['data'];
            _noiDungList =
                listApi.map((json) => NoiDung.fromJson(json)).toList();
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
      setState(() {
        _isLoading = false;
      });
      // Hiển thị thông báo lỗi qua SnackBar
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Lỗi: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Danh sách nội dung'),
        backgroundColor: Colors.blueAccent,
      ),
      body: _isLoading
          ? Center(child: CircularProgressIndicator())
          : ListView.builder(
              itemCount: _noiDungList.length,
              itemBuilder: (context, index) {
                final noiDung = _noiDungList[index];
                return Card(
                  elevation: 3,
                  margin:
                      const EdgeInsets.symmetric(vertical: 8, horizontal: 16),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10),
                  ),
                  child: ListTile(
                    leading: CircleAvatar(
                      backgroundColor: Colors.blueAccent,
                      child: Text(
                        noiDung.id.toString(),
                        style: TextStyle(color: Colors.white),
                      ),
                    ),
                    title: Text(
                      noiDung.tenNoiDung,
                      style:
                          TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                    ),
                    subtitle: Text(noiDung.moTa),
                    trailing:
                        Icon(Icons.arrow_forward_ios, color: Colors.blueAccent),
                    onTap: () {
                      // Thêm hành động khi nhấn vào item
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) =>
                              NoiDungDetailPage(noiDung: noiDung),
                        ),
                      );
                    },
                  ),
                );
              },
            ),
    );
  }
}

class NoiDungDetailPage extends StatelessWidget {
  final NoiDung noiDung;

  NoiDungDetailPage({required this.noiDung});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Chi tiết nội dung"),
        backgroundColor: Colors.blueAccent,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              "Tên nội dung:",
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
            ),
            SizedBox(height: 8),
            Text(noiDung.tenNoiDung, style: TextStyle(fontSize: 16)),
            SizedBox(height: 16),
            Text(
              "Mô tả:",
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
            ),
            SizedBox(height: 8),
            Text(noiDung.moTa, style: TextStyle(fontSize: 16)),
          ],
        ),
      ),
    );
  }
}
