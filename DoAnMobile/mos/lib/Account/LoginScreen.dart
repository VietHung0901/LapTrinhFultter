import 'package:flutter/material.dart';
import '../Admin/AdminHomeScreen.dart';
import '../User/UserHomeScreen.dart';
import 'package:mos/ApiService/Auth/AuthService.dart';
import 'package:mos/Class/LoginRequest.dart';

class LoginScreen extends StatefulWidget {
  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  bool _isLoading = false;
  final TextEditingController usernameController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();
  final AuthService authService = AuthService();

  void handleLogin() async {
    try {
      final request = LoginRequest(
        username: usernameController.text,
        password: passwordController.text,
      );

      setState(() {
        _isLoading = true;
      });

      final response = await authService.login(request);

      setState(() {
        _isLoading = false;
      });

      if (response.role == "MANAGER") {
        // Điều hướng đến trang Home cho Admin
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(builder: (context) => AdminHomeScreen()),
        );
      } else if (response.role == "USER") {
        // Điều hướng đến trang Home cho Thí sinh
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(builder: (context) => UserHomeScreen()),
        );
      }
    } catch (e) {
      // Reset trạng thái _isLoading nếu có lỗi
      setState(() {
        _isLoading = false;
      });

      // Hiển thị lỗi
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('Đăng nhập thất bại! Lỗi: $e'),
      ));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("HỆ THỐNG HỖ TRỢ CUỘC THI MOS"),
        backgroundColor: Colors.blueAccent,
      ),
      body: OrientationBuilder(
        builder: (context, orientation) {
          return SingleChildScrollView(
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: orientation == Orientation.portrait
                    ? CrossAxisAlignment.center
                    : CrossAxisAlignment.start, // Căn chỉnh lại khi quay ngang
                children: [
                  // Logo hoặc tên hệ thống
                  const Center(
                    child: Icon(
                      Icons.school, // Biểu tượng học tập
                      size: 100,
                      color: Colors.blueAccent,
                    ),
                  ),
                  const SizedBox(height: 20),
                  const Text(
                    'XIN CHÀO',
                    style: TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.bold,
                      color: Colors.blueAccent,
                    ),
                  ),
                  const SizedBox(height: 10),
                  Text(
                    'Đăng nhập tại đây',
                    style: TextStyle(
                      fontSize: 16,
                      color: Colors.grey[600],
                    ),
                  ),
                  const SizedBox(height: 30),
                  // Username Input
                  TextField(
                    controller: usernameController,
                    decoration: const InputDecoration(
                      labelText: "Username",
                      prefixIcon: Icon(Icons.person),
                      border: OutlineInputBorder(),
                    ),
                  ),
                  const SizedBox(height: 16),
                  // Password Input
                  TextField(
                    controller: passwordController,
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
                          onPressed: handleLogin,
                          child: const Text("Đăng nhập"),
                          style: ElevatedButton.styleFrom(
                            padding: const EdgeInsets.symmetric(
                                horizontal: 100, vertical: 16),
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
                    child: const Text("Quên mật khẩu"),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}
