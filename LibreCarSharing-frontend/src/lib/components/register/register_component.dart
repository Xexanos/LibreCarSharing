import 'dart:async';
import 'package:angular2/router.dart';

import 'package:LibreCarSharingFrontend/services/user_service.dart';
import 'package:angular2/angular2.dart';

@Component(
  selector: 'register',
  styleUrls: const ['register_component.css'],
  templateUrl: 'register_component.html',
)
class RegisterComponent implements OnInit {
  String username = "";
  String password = "";
  String email = "";

  final Router _router;
  final UserService _userService;

  RegisterComponent(this._router, this._userService);

  @override
  Future<Null> ngOnInit() async {}

  register(dynamic e) async {
    e.preventDefault();

    int status = await _userService.registerUser(username, password, email);
    switch (status){
      case 200:
        _router.navigate(['Login']);
        break;
      default:
        print("Status code is " + status.toString());
    }
    //TODO: react to status
  }
}
