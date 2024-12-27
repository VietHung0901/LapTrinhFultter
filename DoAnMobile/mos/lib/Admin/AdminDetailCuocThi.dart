import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import 'package:mos/Admin/AdminScanDiem.dart';

class CuocThiDetailScreen extends StatefulWidget {
  final int contestId;

  CuocThiDetailScreen({required this.contestId});

  @override
  _CuocThiDetailScreenState createState() => _CuocThiDetailScreenState();
}

class _CuocThiDetailScreenState extends State<CuocThiDetailScreen> {
  bool _isLoading = true;
  dynamic _contestDetail;

  // Lấy thông tin chi tiết cuộc thi từ API
  Future<void> _fetchContestDetail() async {
    final response = await http.get(Uri.parse('http://localhost:8080/api/cuocThi/${widget.contestId}'));

    if (response.statusCode == 200) {
      setState(() {
        _contestDetail = json.decode(response.body);
        _isLoading = false;
      });
    } else {
      setState(() {
        _isLoading = false;
      });
      throw Exception('Failed to load contest details');
    }
  }

  @override
  void initState() {
    super.initState();
    _fetchContestDetail();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Chi tiết cuộc thi'),
        backgroundColor: Colors.blueAccent,
      ),
      body: _isLoading
          ? Center(child: CircularProgressIndicator())
          : Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Tên cuộc thi
                  Text(
                    _contestDetail['tenCuocThi'],
                    style: TextStyle(
                      fontSize: 26,
                      fontWeight: FontWeight.bold,
                      color: Colors.blueAccent,
                    ),
                  ),
                  const SizedBox(height: 20),
                  
                  // Thông tin cuộc thi
                  _buildInfoRow('Ngày thi', _contestDetail['ngayThi']),
                  _buildInfoRow('Số lượng thí sinh', _contestDetail['soLuongThiSinh'].toString()),
                  _buildInfoRow('Địa điểm thi', _contestDetail['diaDiemThi']),
                  
                  const SizedBox(height: 30),

                  // Nút nhập điểm
                  ElevatedButton(
                    onPressed: () {
                      // Thực hiện hành động nhập điểm
                      Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) =>
                          CameraApp()), // Điều hướng đến màn hình tới scan điểm
                );
                    },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.blueAccent,
                      padding: EdgeInsets.symmetric(vertical: 12, horizontal: 24),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8.0),
                      ),
                    ),
                    child: const Text(
                      'Nhập Điểm',
                      style: TextStyle(fontSize: 18),
                    ),
                  ),
                  
                ],
              ),
            ),
    );
  }

  // Helper widget để xây dựng thông tin cuộc thi
  Widget _buildInfoRow(String title, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 10.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(
            title,
            style: TextStyle(fontSize: 18, fontWeight: FontWeight.w500),
          ),
          Text(
            value,
            style: TextStyle(fontSize: 18, color: Colors.grey[700]),
          ),
        ],
      ),
    );
  }
}
