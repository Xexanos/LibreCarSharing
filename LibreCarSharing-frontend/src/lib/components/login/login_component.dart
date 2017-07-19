import 'dart:async';

import 'package:angular2/angular2.dart';

import 'package:angular2/router.dart';

import 'package:LibreCarSharingFrontend/services/user_service.dart';

@Component(
  selector: 'login',
  styleUrls: const ['login_component.css'],
  templateUrl: 'login_component.html',
)
class LoginComponent implements OnInit {
  String username = "";
  String password = "";

  final UserService _userService;
  final Router _router;

  LoginComponent(this._userService, this._router);

  @override
  Future<Null> ngOnInit() async {
    // TODO: implement ngOnInit
  }

  void login(dynamic e){
    e.preventDefault();
    this._userService.login(this.username, this.password);
  }

  void routeRegister(dynamic e) {
    e.preventDefault();
    _router.navigate(["Register"]);
  }
}
