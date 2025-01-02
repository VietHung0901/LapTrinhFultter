import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:mos/Class/LoginRequest.dart';
import 'package:mos/Class/LoginResponse.dart';
import 'package:mos/Class/StringURL.dart';
import 'package:shared_preferences/shared_preferences.dart';

class AuthService {
  final String baseUrl;

  AuthService() : baseUrl = StringURL().baseUrl;

  Future<void> saveToken(String accesstoken, String refreshtoken) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('auth_token', accesstoken);
    print("AccessToken: " + accesstoken);
    await prefs.setString('refresh_token', refreshtoken);
  }

  Future<LoginResponse> login(LoginRequest request) async {
    final url = Uri.parse('$baseUrl/api/login');

    final response = await http.post(
      url,
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(request.toJson()),
    );

    if (response.statusCode == 200) {

      // Parse JSON khi đăng nhập thành công
      final json = jsonDecode(response.body);
      saveToken(LoginResponse.fromJson(json).accesstoken, LoginResponse.fromJson(json).refreshtoken);

      return LoginResponse.fromJson(json);
    } else {
      // Xử lý lỗi
      final error = jsonDecode(response.body);
      throw Exception(error['message']);
    }
  }

  Future<void> _refreshToken() async {
    // Implement refresh token logic
    final prefs = await SharedPreferences.getInstance();
    final refreshToken = prefs.getString('refresh_token');
    if (refreshToken != null) {
      final response = await http.post(
        Uri.parse('$baseUrl/refresh-token'),
        body: jsonEncode({'refreshToken': refreshToken}),
        headers: {'Content-Type': 'application/json'},
      );

      if (response.statusCode == 200) {
        final newToken = json.decode(response.body)['accesstoken'];
        final newRefreshToken = json.decode(response.body)['refreshtoken'];
        await prefs.setString('auth_token', newToken);
        await prefs.setString('refresh_token', newRefreshToken);
        print('Tokens refreshed successfully');
      } else {
        throw Exception('Failed to refresh token');
      }
    } else {
      throw Exception('No refresh token available');
    }
  }
}
