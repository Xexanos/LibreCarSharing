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

  final UserService _userService;

  UserDisplayComponent(this._userService);

  @override
  Future<Null> ngOnInit() async {
    _userService.getCurrentUser().then((user) {
      this.user = user;
    });
  }

  void sendChanges(dynamic e) {
    //TODO: Send changes to backend
  }
}
