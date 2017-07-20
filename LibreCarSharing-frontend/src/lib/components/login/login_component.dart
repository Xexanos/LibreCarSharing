import 'dart:async';

import 'package:angular2/angular2.dart';

import 'package:angular2/router.dart';

import 'package:LibreCarSharingFrontend/services/user_service.dart';

@Component(
  selector: 'login',
  styleUrls: const ['login_component.css'],
  templateUrl: 'login_component.html',
  directives: const [ROUTER_DIRECTIVES],
)
class LoginComponent implements OnInit {
  String username = "";
  String password = "";

  final UserService _userService;

  LoginComponent(this._userService);

  @override
  Future<Null> ngOnInit() async {}

  void login(dynamic e) {
    e.preventDefault();
    this._userService.login(this.username, this.password);
  }

}
