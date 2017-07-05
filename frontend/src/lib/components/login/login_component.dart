import 'dart:async';
import 'dart:html';

import 'package:angular2/angular2.dart';

import '../../model/user.dart';

@Component(
  selector: 'login',
  styleUrls: const ['login_component.css'],
  templateUrl: 'login_component.html',
)
class LoginComponent implements OnInit {
  User user = new User();
  //bool isLoggedIn() => loggedIn;

  @override
  Future<Null> ngOnInit() async {
    // TODO: implement ngOnInit
  }

  void login(dynamic e){
    e.preventDefault();
    HttpRequest.postFormData("../login.jsp", { "username" : this.user.username, "password" : this.user.password })
      //U.then((request) {loggedIn = true;})
      .catchError((n)=>print(n));
  }
}
