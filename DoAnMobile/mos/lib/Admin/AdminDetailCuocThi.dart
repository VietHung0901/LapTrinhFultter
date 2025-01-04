import 'package:flutter/material.dart';
import 'package:mos/Admin/AdminPhieuDangKy.dart';
import 'dart:convert';

import 'package:mos/Admin/AdminScanDiem.dart';
import 'package:mos/ApiService/Auth/AuthService.dart';
import 'package:mos/ApiService/HTTPService.dart';
import 'package:mos/Class/StringURL.dart';

class CuocThiDetailScreen extends StatefulWidget {
  final int contestId;

  CuocThiDetailScreen({required this.contestId});

  @override
  _CuocThiDetailScreenState createState() => _CuocThiDetailScreenState();
}

class _CuocThiDetailScreenState extends State<CuocThiDetailScreen> {
  dynamic _contestDetail;
  final HTTPService httpService = HTTPService();
  bool _isLoading = true;
  AuthService authservice = AuthService();

  @override
  void initState() {
    super.initState();
    _fetchContestDetail();
  }

  // Lấy thông tin chi tiết cuộc thi từ API
  Future<void> _fetchContestDetail() async {
    final response = await httpService
        .get(StringURL().admincuocthi + '/detail/${widget.contestId}');
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
          }
          setState(() {
            // Lấy detail cuộc thi từ phần "data"
            _contestDetail = responseData['data'];
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
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Lỗi: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Chi tiết cuộc thi'),
        backgroundColor: Colors.blueAccent,
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : Padding(
              padding: const EdgeInsets.all(16.0),
              child: SingleChildScrollView(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // Tiêu đề
                    Center(
                      child: Text(
                        _contestDetail['tenCuocThi'],
                        textAlign: TextAlign.center,
                        style: const TextStyle(
                          fontSize: 28,
                          fontWeight: FontWeight.bold,
                          color: Colors.blueAccent,
                        ),
                      ),
                    ),
                    const SizedBox(height: 24),

                    // Card thông tin cuộc thi
                    Card(
                      elevation: 8,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(16.0),
                      ),
                      child: Padding(
                        padding: const EdgeInsets.all(16.0),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            _buildInfoRow(
                                'Ngày thi', _contestDetail['ngayThi']),
                            const Divider(),
                            _buildInfoRow('Số lượng thí sinh',
                                _contestDetail['soLuongThiSinh'].toString()),
                            const Divider(),
                            _buildInfoRow(
                                'Địa điểm thi', _contestDetail['diaDiemThi']),
                          ],
                        ),
                      ),
                    ),

                    const SizedBox(height: 36),
                    Wrap(
                      spacing: 16,
                      alignment: WrapAlignment.center,
                      children: [
                        ElevatedButton.icon(
                          onPressed: () {
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (context) => CameraScreen(
                                    contestId: _contestDetail['id']),
                              ),
                            );
                          },
                          icon: const Icon(Icons.camera_alt),
                          label: const Text('Nhập điểm'),
                          style: ElevatedButton.styleFrom(
                            padding: const EdgeInsets.symmetric(
                                vertical: 14, horizontal: 24),
                            backgroundColor: Colors.blueAccent,
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(12),
                            ),
                            textStyle: const TextStyle(fontSize: 16),
                          ),
                        ),
                        ElevatedButton.icon(
                          onPressed: () {
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (context) =>
                                    ResultPage(cuocThiId: _contestDetail['id']),
                              ),
                            );
                          },
                          icon: const Icon(Icons.list_alt),
                          label: const Text('Danh sách'),
                          style: ElevatedButton.styleFrom(
                            padding: const EdgeInsets.symmetric(
                                vertical: 14, horizontal: 24),
                            backgroundColor: Colors.blueAccent,
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(12),
                            ),
                            textStyle: const TextStyle(fontSize: 16),
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 16),
                  ],
                ),
              ),
            ),
    );
  }

  Widget _buildInfoRow(String title, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Expanded(
            flex: 2,
            child: Text(
              title,
              style: const TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.w500,
                color: Colors.black87,
              ),
            ),
          ),
          Expanded(
            flex: 3,
            child: Text(
              value,
              textAlign: TextAlign.right,
              style: TextStyle(
                fontSize: 18,
                color: Colors.grey[700],
              ),
              overflow: TextOverflow.ellipsis,
              maxLines: 1,
            ),
          ),
        ],
      ),
    );
  }
}
