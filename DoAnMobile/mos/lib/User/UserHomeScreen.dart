import 'package:flutter/material.dart';
import 'package:mos/Account/LoginScreen.dart';
import 'package:shared_preferences/shared_preferences.dart';

class UserHomeScreen extends StatelessWidget {
  // Hàm logout
  Future<void> logout(BuildContext context) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('auth_token');  // Xóa token khỏi SharedPreferences

    // Điều hướng về màn hình đăng nhập
    Navigator.pushReplacement(
      context,
      MaterialPageRoute(builder: (context) => LoginScreen()),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text(
          "Contestant Dashboard",
          style: TextStyle(fontWeight: FontWeight.bold),
        ),
        backgroundColor: Colors.orangeAccent,
        elevation: 4.0,
        actions: [
          // Thêm icon Logout trên appBar
          IconButton(
            icon: const Icon(Icons.exit_to_app),
            onPressed: () {
              logout(context);  // Xử lý logout khi nhấn vào biểu tượng
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
              'Welcome, Contestant!',
              style: TextStyle(
                fontSize: 28,
                fontWeight: FontWeight.bold,
                color: Colors.orangeAccent,
              ),
            ),
            const SizedBox(height: 40),

            // Card with "View Exam Results"
            _buildCard(
              context,
              title: 'View Exam Results',
              icon: Icons.assignment_turned_in,
              onPressed: () {
                // Xem kết quả thi
              },
            ),
          ],
        ),
      ),
    );
  }

  // Helper method to create cards
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
          color: Colors.orangeAccent,
        ),
        title: Text(
          title,
          style: const TextStyle(
            fontSize: 18,
            fontWeight: FontWeight.bold,
            color: Colors.black,
          ),
        ),
        trailing: const Icon(Icons.arrow_forward, color: Colors.orangeAccent),
        onTap: onPressed,
      ),
    );
  }
}
