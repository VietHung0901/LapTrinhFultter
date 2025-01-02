class LoginResponse {
  final String status;
  final String message;
  final String accesstoken;
  final String refreshtoken;
  final String username;
  final String role;

  LoginResponse({
    required this.status,
    required this.message,
    required this.accesstoken,
    required this.refreshtoken,
    required this.username,
    required this.role,
  });

  factory LoginResponse.fromJson(Map<String, dynamic> json) {
    return LoginResponse(
      status: json['status'],
      message: json['message'],
      accesstoken: json['data']['accesstoken'],
      refreshtoken: json['data']['refreshtoken'],
      username: json['data']['username'],
      role: json['data']['role'],
    );
  }
}
