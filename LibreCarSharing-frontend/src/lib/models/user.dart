import 'dart:convert';
class User {
  String username;
  String displayName;
  String email;
  String password;
  String imageFile;

  String toJSON() {
    return JSON.encode({'username':username, 'email':email, 'password':password, 'displayName':displayName});
  }
}