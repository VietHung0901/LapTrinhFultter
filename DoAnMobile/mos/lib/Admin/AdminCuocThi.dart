import 'package:flutter/material.dart';
import 'dart:convert';

import 'package:mos/Admin/AdminDetailCuocThi.dart';
import 'package:mos/ApiService/HTTPService.dart';
import 'package:mos/Class/StringURL.dart';

class CuocThiListScreen extends StatefulWidget {
  @override
  _CuocThiListScreenState createState() => _CuocThiListScreenState();
}

class _CuocThiListScreenState extends State<CuocThiListScreen> {
  bool _isLoading = true;
  List<dynamic> _contests = [];
  final HTTPService httpService = HTTPService();
  
  // Gọi API tải cuộc thi
  Future<void> _fetchContests() async {
    setState(() {
      _isLoading = true;
    });

    try {
      final response = await httpService.get(StringURL().admincuocthi + '/list');

      if (response.statusCode == 200) {
        setState(() {
          _contests = json.decode(response.body);
          _isLoading = false;
        });
      } else {
        // throw Exception('Failed to load contests');
        throw Exception(response.statusCode);
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
    _fetchContests();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Danh sách cuộc thi'),
        backgroundColor: Colors.blueAccent,
        elevation: 0,
      ),
      body: _isLoading
          ? Center(child: CircularProgressIndicator())
          : Padding(
              padding: const EdgeInsets.all(8.0),
              child: ListView.builder(
                itemCount: _contests.length,
                itemBuilder: (context, index) {
                  return _buildContestCard(_contests[index]);
                },
              ),
            ),
    );
  }

  // Card hiển thị thông tin cuộc thi
  Widget _buildContestCard(dynamic contest) {
  return Card(
    elevation: 6.0,
    margin: EdgeInsets.symmetric(vertical: 12.0, horizontal: 16.0),
    shape: RoundedRectangleBorder(
      borderRadius: BorderRadius.circular(15.0),
    ),
    child: InkWell(
      onTap: () {
        // Chuyển tới màn hình chi tiết cuộc thi
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) =>
                CuocThiDetailScreen(contestId: contest['id']),
          ),
        );
      },
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Icon đại diện cho cuộc thi
            Container(
              width: 70,
              height: 70,
              decoration: BoxDecoration(
                color: Colors.blueAccent,
                borderRadius: BorderRadius.circular(15.0),
                boxShadow: [
                  BoxShadow(
                    color: Colors.blueAccent.withOpacity(0.2),
                    spreadRadius: 2,
                    blurRadius: 5,
                  ),
                ],
              ),
              child: Icon(
                Icons.emoji_events,
                color: Colors.white,
                size: 40,
              ),
            ),
            const SizedBox(width: 16),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Tên cuộc thi và ID
                  Row(
                    children: [
                      // ID cuộc thi
                      Text(
                        'ID: ${contest['id']}',
                        style: TextStyle(
                          fontSize: 14,
                          color: Colors.blueAccent,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                      const SizedBox(width: 8),
                      // Tên cuộc thi
                      Text(
                        contest['tenCuocThi'],
                        style: TextStyle(
                          fontSize: 18,
                          fontWeight: FontWeight.bold,
                          color: Colors.black87,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 6),
                  // Ngày thi
                  Text(
                    'Ngày thi: ${contest['ngayThi']}',
                    style: TextStyle(
                      fontSize: 15,
                      color: Colors.grey[600],
                    ),
                  ),
                ],
              ),
            ),
            // Mũi tên chỉ dẫn
            Icon(
              Icons.arrow_forward_ios,
              color: Colors.blueAccent,
              size: 20,
            ),
          ],
        ),
      ),
    ),
  );
}

}
