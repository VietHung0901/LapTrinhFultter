import 'package:flutter/material.dart';
import 'package:mos/ApiService/ApiUserService.dart';
import '../Admin/AdminHomeScreen.dart';
import '../User/UserHomeScreen.dart';

class LoginScreen extends StatefulWidget {
  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  bool _isLoading = false;
  final ApiUserService apiUserService = ApiUserService('http://localhost:8080'); // URL của Spring Boot

  // Mock function for login
  Future<void> _login() async {
    setState(() {
      _isLoading = true;
    });

// Gọi API login
    bool isSuccess = await apiUserService.login(
      _usernameController.text, 
      _passwordController.text,
    );

    setState(() {
      _isLoading = false;
    });

    if (isSuccess) {
      String userRole = await apiUserService.getUserRole(_usernameController.text);  // API kiểm tra quyền của người dùng

      if (userRole == "MANAGER") {
        // Điều hướng đến trang Home cho Admin
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(builder: (context) => AdminHomeScreen()),
        );
      } else if (userRole == "USER") {
        // Điều hướng đến trang Home cho Thí sinh
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(builder: (context) => UserHomeScreen()),
        );
      }
    } else {
      // Nếu đăng nhập thất bại, hiển thị thông báo lỗi
      showDialog(
        context: context,
        builder: (context) => AlertDialog(
          title: const Text("Login Failed"),
          content: const Text("Invalid username or password."),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text("Try Again"),
            ),
          ],
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("MOS Support System"),
        backgroundColor: Colors.blueAccent,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            // Logo hoặc tên hệ thống
            Center(
              child: Icon(
                Icons.school, // Biểu tượng học tập
                size: 100,
                color: Colors.blueAccent,
              ),
            ),
            const SizedBox(height: 20),
            Text(
              'Welcome to the MOS Support System!',
              style: TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
                color: Colors.blueAccent,
              ),
            ),
            const SizedBox(height: 10),
            Text(
              'Please log in to start the support',
              style: TextStyle(
                fontSize: 16,
                color: Colors.grey[600],
              ),
            ),
            const SizedBox(height: 30),
            // Username Input
            TextField(
              controller: _usernameController,
              decoration: const InputDecoration(
                labelText: "Username",
                prefixIcon: Icon(Icons.person),
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 16),
            // Password Input
            TextField(
              controller: _passwordController,
              obscureText: true,
              decoration: const InputDecoration(
                labelText: "Password",
                prefixIcon: Icon(Icons.lock),
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 24),
            // Loading Indicator or Login Button
            _isLoading
                ? const CircularProgressIndicator()
                : ElevatedButton(
                    onPressed: _login,
                    child: const Text("Login"),
                    style: ElevatedButton.styleFrom(
                      padding: const EdgeInsets.symmetric(horizontal: 100, vertical: 16),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8),
                      ),
                       backgroundColor: Colors.blueAccent,
                    ),
                  ),
            const SizedBox(height: 20),
            // Option to reset password or contact admin
            TextButton(
              onPressed: () {
                // Action khi quên mật khẩu hoặc cần trợ giúp
                Navigator.pushNamed(context, '/help');
              },
              child: const Text("Forgot password? Contact admin"),
            ),
          ],
        ),
      ),
    );
  }
}
