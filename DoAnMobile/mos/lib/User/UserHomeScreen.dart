import 'package:flutter/material.dart';
import 'package:mos/ApiService/Auth/AuthService.dart';
import 'package:mos/User/PhieuDangKy.dart';
import 'package:mos/User/UserCuocThi.dart';

class UserHomeScreen extends StatelessWidget {
  final AuthService authservice;
  UserHomeScreen({Key? key, required this.authservice}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text(
          "Trang chủ",
          style: TextStyle(fontWeight: FontWeight.bold),
        ),
        backgroundColor: Colors.blueAccent,
        elevation: 4.0,
        actions: [
          // Thêm icon Logout trên appBar
          IconButton(
            icon: const Icon(Icons.exit_to_app),
            onPressed: () {
              authservice.logout(context); // Xử lý logout khi nhấn vào biểu tượng
            },
          ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            // Welcome message
            const Text(
              'Xin chào, thí sinh!',
              style: TextStyle(
                fontSize: 28,
                fontWeight: FontWeight.bold,
                color: Colors.blueAccent,
              ),
            ),
            const SizedBox(height: 40),

            _buildCard(
              context,
              title: 'Danh sách cuộc thi',
              icon: Icons.emoji_events,
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) =>
                          CuocThiListScreen()), // Điều hướng đến màn hình danh sách cuộc thi
                );
              },
            ),

            const SizedBox(height: 20),
            // Card with "View Exam Results"
            _buildCard(
              context,
              title: 'Phiếu đăng ký',
              icon: Icons.assignment_turned_in,
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) =>
                          PhieuDangKyPage()), // Điều hướng đến màn hình danh sách cuộc thi
                );
              },
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildCard(BuildContext context, {required String title, required IconData icon, required VoidCallback onPressed}) {
    return Card(
      elevation: 8.0,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(15.0),
      ),
      child: ListTile(
        contentPadding: const EdgeInsets.all(16.0),
        leading: Icon(
          icon,
          size: 40,
          color: Colors.blueAccent,
        ),
        title: Text(
          title,
          style: const TextStyle(
            fontSize: 18,
            fontWeight: FontWeight.bold,
            color: Colors.black,
          ),
        ),
        trailing: const Icon(Icons.arrow_forward, color: Colors.blueAccent),
        onTap: onPressed,
      ),
    );
  }
}
