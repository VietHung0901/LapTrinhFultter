class LoginResponse {
  final String status;
  final String message;
  final String token;
  final String username;
  final String role;

  LoginResponse({
    required this.status,
    required this.message,
    required this.token,
    required this.username,
    required this.role,
  });

  factory LoginResponse.fromJson(Map<String, dynamic> json) {
    return LoginResponse(
      status: json['status'],
      message: json['message'],
      token: json['data']['token'],
      username: json['data']['username'],
      role: json['data']['role'],
    );
  }
}
