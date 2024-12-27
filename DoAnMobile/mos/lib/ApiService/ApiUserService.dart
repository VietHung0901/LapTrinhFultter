import 'package:http/http.dart' as http;
import 'dart:convert';

class ApiUserService {
  final String baseUrl;

  ApiUserService(this.baseUrl);

  Future<bool> login(String username, String password) async {
    final response = await http.post(
      Uri.parse('$baseUrl/api/login'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'username': username, 'password': password}),
    );

    if (response.statusCode == 200) {
      // return response.body;  // Trả về token JWT
      return true;
    } else {
      // throw Exception('Login failed');
      return false;
    }
  }

  // Hàm lấy quyền người dùng
  Future<String> getUserRole(String username) async {
    final response = await http.get(
      Uri.parse('$baseUrl/api/getRole?username=$username'),
    );

    if (response.statusCode == 200) {
      return response.body;  // Trả về vai trò của người dùng
    } else {
      print(response.statusCode);
      throw Exception('Failed to load user role');
    }
  }
}
