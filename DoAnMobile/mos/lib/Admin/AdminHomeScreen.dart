import 'package:flutter/material.dart';
import 'package:mos/Admin/AdminCuocThi.dart';
import 'package:mos/ApiService/Auth/AuthService.dart';

class AdminHomeScreen extends StatelessWidget {
final AuthService authservice;
  AdminHomeScreen({Key? key, required this.authservice}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text(
          "Trang chủ Admin",
          style: TextStyle(fontWeight: FontWeight.bold),
        ),
        backgroundColor: Colors.blueAccent,
        elevation: 4.0,
        actions: [
          IconButton(
            icon: const Icon(Icons.exit_to_app),
            onPressed: () {
              authservice.logout(context);  // Xử lý logout khi nhấn vào biểu tượng
            },
          ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            // Title: Welcome message
            const Text(
              'Xin chào, Admin!',
              style: TextStyle(
                fontSize: 28,
                fontWeight: FontWeight.bold,
                color: Colors.blueAccent,
              ),
            ),
            const SizedBox(height: 30),

            _buildCard(
              context,
              title: 'Quản lý cuộc thi',
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

            // Card with "Manage Scores"
            // _buildCard(
            //   context,
            //   title: 'Manage Scores',
            //   icon: Icons.score,
            //   onPressed: () {
            //     // Handle managing scores
            //   },
            // ),
          ],
        ),
      ),
    );
  }

  Widget _buildCard(BuildContext context,
      {required String title,
      required IconData icon,
      required VoidCallback onPressed}) {
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
