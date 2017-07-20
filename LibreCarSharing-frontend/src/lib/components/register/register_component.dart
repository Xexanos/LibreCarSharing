import 'dart:async';

import 'package:LibreCarSharingFrontend/services/user_service.dart';
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

  final UserService _userService;

  RegisterComponent(this._userService);

  @override
  Future<Null> ngOnInit() async {}

  register(dynamic e) async {
    e.preventDefault();
    int status = await _userService.registerUser(username, password, email);
    //TODO: react to status
  }
}
