import 'dart:async';

import 'package:angular2/angular2.dart';

import 'package:LibreCarSharingFrontend/models/user.dart';

import 'package:LibreCarSharingFrontend/services/user_service.dart';

@Component(
  selector: 'login',
  styleUrls: const ['login_component.css'],
  templateUrl: 'login_component.html',
)
class LoginComponent implements OnInit {
  User user = new User();

  final UserService _userService;

  LoginComponent(this._userService);

  @override
  Future<Null> ngOnInit() async {
    // TODO: implement ngOnInit
  }

  void login(dynamic e){
    this._userService.login(e, this.user);
  }
}
