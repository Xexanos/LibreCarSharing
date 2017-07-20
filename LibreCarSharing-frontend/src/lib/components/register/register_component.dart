import 'dart:async';
import 'dart:html';

import 'package:angular2/angular2.dart';

@Component(
  selector: 'register',
  styleUrls: const ['register_component.css'],
  templateUrl: 'register_component.html',
)
class RegisterComponent implements OnInit {
  String username;
  String password;
  String email;

  @override
  Future<Null> ngOnInit() async {}

  // TODO: move to user_service
  void register(dynamic e) {
    e.preventDefault();
    HttpRequest.postFormData("/register", {
      "username": this.username,
      "displayName": this.username,
      "password": this.password,
      "email": this.email
    })
        .then((request) {})
        .catchError((n) => print(n));
  }
}
