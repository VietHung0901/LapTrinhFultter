import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:mos/Account/LoginScreen.dart';
import 'package:mos/Class/LoginRequest.dart';
import 'package:mos/Class/LoginResponse.dart';
import 'package:mos/Class/StringURL.dart';
import 'package:shared_preferences/shared_preferences.dart';

class AuthService {
  final String baseUrl;

  AuthService() : baseUrl = StringURL().baseUrl;

  Future<void> saveToken(String token) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('auth_token', token);
    print("Token: " + token);
  }

  Future<LoginResponse> login(LoginRequest request) async {
    final url = Uri.parse('$baseUrl/api/auth/login');

    final response = await http.post(
      url,
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(request.toJson()),
    );

    if (response.statusCode == 200) {

      // Parse JSON khi đăng nhập thành công
      final json = jsonDecode(response.body);
      saveToken(LoginResponse.fromJson(json).token);

      return LoginResponse.fromJson(json);
    } else {
      // Xử lý lỗi
      final error = jsonDecode(response.body);
      throw Exception(error['message']);
    }
  }

  Future<void> logout(BuildContext context) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('auth_token'); // Xóa token khỏi SharedPreferences

    // Điều hướng về màn hình đăng nhập
    Navigator.pushReplacement(
      context,
      MaterialPageRoute(builder: (context) => LoginScreen()),
    );
  }
}
