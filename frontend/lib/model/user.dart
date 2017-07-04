import 'dart:convert';
class User {
  String username = "";
  String password = "";
  bool stay = true;

  String toJSON() {
    return JSON.encode({'username':username, 'password':password, 'stay':stay});
  }
}