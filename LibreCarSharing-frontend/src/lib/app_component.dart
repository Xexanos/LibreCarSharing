import 'dart:html';
import 'package:LibreCarSharingFrontend/components/car_display/car_display_component.dart';
import 'package:LibreCarSharingFrontend/components/dashboard/dashboard_component.dart';
import 'package:LibreCarSharingFrontend/components/login/login_component.dart';
import 'package:LibreCarSharingFrontend/components/register/register_component.dart';
import 'package:LibreCarSharingFrontend/components/sidebar/sidebar_component.dart';
import 'package:LibreCarSharingFrontend/components/user_display/user_display_component.dart';
import 'package:LibreCarSharingFrontend/implementation/user_impl.dart';
import 'package:LibreCarSharingFrontend/services/car_service.dart';
import 'package:LibreCarSharingFrontend/services/tab_service.dart';
import 'package:LibreCarSharingFrontend/services/user_service.dart';
import 'package:angular2/angular2.dart';
import 'package:angular2/router.dart';
import 'package:ng_bootstrap/ng_bootstrap.dart';

// AngularDart info: https://webdev.dartlang.org/angular
// Components info: https://webdev.dartlang.org/components

@Component(
  selector: 'my-app',
  styleUrls: const ['app_component.css'],
  templateUrl: 'app_component.html',
  directives: const [BS_DIRECTIVES, ROUTER_DIRECTIVES, SidebarComponent],
  providers: const [ROUTER_PROVIDERS, CarService, UserService, TabService],
)
@RouteConfig(const [
  const Route(path: '/login', name: 'Login', component: LoginComponent),
  const Route(path: '/register', name: 'Register', component: RegisterComponent),
  const Route(path: '/dashboard', name: 'Dashboard', component: DashboardComponent),
  const Route(path: '/car/:id', name: 'Car', component: CarDisplayComponent),
  const Route(path: '/user/:id', name: 'User', component: UserDisplayComponent)
])
class AppComponent {
  String title = "LibreCarSharing";
  bool debug = false;
  UserImpl user;

  final UserService _userService;
  final Router _router;

  AppComponent(this._userService, this._router) {
    this._userService.userStream.listen((UserImpl user) {
      this.user = user;
      if (this.user == null) {
        _router.navigate(['Login']);
      } else {
        _router.navigate(['Dashboard']);
      }
    });

    this.user = _userService.getCurrentUser();
    if (this.user == null) {
      _router.navigate(['Login']);
    } else {
      _router.navigate(['Dashboard']);
    }
  }
}
