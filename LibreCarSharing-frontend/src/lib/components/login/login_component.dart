import 'dart:async';
import 'dart:html';

import 'package:angular2/angular2.dart';

import 'package:frontend/model/user.dart';

@Component(
  selector: 'login',
  styleUrls: const ['login_component.css'],
  templateUrl: 'login_component.html',
)
class LoginComponent implements OnInit {
  User user = new User();
  bool loggedIn = false;

  @override
  Future<Null> ngOnInit() async {
    // TODO: implement ngOnInit
  }

  void login(dynamic e){
    e.preventDefault();
    HttpRequest.postFormData("../login.jsp", { "username" : this.user.username, "password" : this.user.password })
      .then((request) {loggedIn = true;})
      .catchError((n)=>print(n));
  }

  void logout(dynamic e){
    e.preventDefault();
    HttpRequest.request("../logout", method: "GET")
        .then((request) {loggedIn = false; print(request.getAllResponseHeaders());})
        .catchError((n)=>print(n));
  }
}
