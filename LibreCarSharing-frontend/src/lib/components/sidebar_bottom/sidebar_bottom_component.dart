import 'dart:async';

import 'package:angular2/angular2.dart';
import 'package:angular2/router.dart';

import 'package:LibreCarSharingFrontend/interfaces/user.dart';

import 'package:LibreCarSharingFrontend/services/user_service.dart';

@Component(
  selector: 'sidebarBottom',
  styleUrls: const ['sidebar_bottom_component.css'],
  templateUrl: 'sidebar_bottom_component.html',
  directives: const [ROUTER_DIRECTIVES],
)
class SidebarBottomComponent implements OnInit {
  @Input("user")
  User user;

  final Router _router;
  final UserService _userService;

  SidebarBottomComponent(this._router, this._userService);

  @override
  Future<Null> ngOnInit() async {}

  void logout(dynamic e) {
    e.preventDefault();
    this._userService.logout();
  }

  void displayUser(dynamic e) {
    e.preventDefault();
    this._router.navigate(['User']);
  }

  void newCar(dynamic e) {
    e.preventDefault();
    this._router.navigate(['NewCar']);
  }
}
