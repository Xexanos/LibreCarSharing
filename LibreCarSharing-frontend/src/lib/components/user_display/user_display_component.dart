import 'dart:async';

import 'package:LibreCarSharingFrontend/interfaces/user.dart';

import 'package:LibreCarSharingFrontend/services/user_service.dart';
import 'package:angular2/angular2.dart';
import 'package:angular2/router.dart';

@Component(
  selector: 'userDisplay',
  styleUrls: const ['user_display_component.css'],
  templateUrl: 'user_display_component.html',
  directives: const [COMMON_DIRECTIVES],
)
class UserDisplayComponent implements OnInit {
  User user;

  final RouteParams _routeParams;
  final UserService _userService;

  UserDisplayComponent(this._routeParams, this._userService);

  @override
  Future<Null> ngOnInit() async {
    var _id = _routeParams.get('id');
    var id = int.parse(_id ?? '', onError: (_) => null);
    if (id != null) user = await (_userService.getUser(id));

    // TODO: implement ngOnInit
  }

  void sendChanges(dynamic e) {
    //TODO: Send changes to backend
  }
}
