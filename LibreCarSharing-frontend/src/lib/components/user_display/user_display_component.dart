import 'dart:async';

import 'package:LibreCarSharingFrontend/interfaces/user.dart';

import 'package:LibreCarSharingFrontend/services/user_service.dart';
import 'package:angular2/angular2.dart';

@Component(
  selector: 'userDisplay',
  styleUrls: const ['user_display_component.css'],
  templateUrl: 'user_display_component.html',
  directives: const [COMMON_DIRECTIVES],
)
class UserDisplayComponent implements OnInit {
  User user = null;
  String password = "";
  String newPassword = "";
  String newPassword2 = "";

  final UserService _userService;

  UserDisplayComponent(this._userService);

  @override
  Future<Null> ngOnInit() async {
    user = await _userService.getCurrentUser();
  }

  sendChanges(dynamic e) async {
    e.preventDefault();
    if (newPassword == newPassword2) {
      user = await _userService.changeUser(user, password, newPassword);
    }
  }
}
