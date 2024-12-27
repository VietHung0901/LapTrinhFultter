import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import 'package:mos/Admin/AdminDetailCuocThi.dart';

class CuocThiListScreen extends StatefulWidget {
  @override
  _CuocThiListScreenState createState() => _CuocThiListScreenState();
}

class _CuocThiListScreenState extends State<CuocThiListScreen> {
  bool _isLoading = true;
  List<dynamic> _contests = [];

  // Lấy danh sách cuộc thi từ API
  Future<void> _fetchContests() async {
    final response =
        await http.get(Uri.parse('http://localhost:8080/api/cuocThi'));

    if (response.statusCode == 200) {
      setState(() {
        _contests = json.decode(response.body);
        _isLoading = false;
      });
    } else {
      setState(() {
        _isLoading = false;
      });
      throw Exception('Failed to load contests');
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
      elevation: 5.0,
      margin: EdgeInsets.symmetric(vertical: 10.0),
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
            children: [
              Icon(
                Icons.emoji_events,
                size: 50,
                color: Colors.blueAccent,
              ),
              const SizedBox(width: 16),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      contest['tenCuocThi'],
                      style: TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                        color: Colors.black87,
                      ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'Ngày thi: ${contest['ngayThi']}',
                      style: TextStyle(
                        fontSize: 16,
                        color: Colors.grey[600],
                      ),
                    ),
                  ],
                ),
              ),
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
