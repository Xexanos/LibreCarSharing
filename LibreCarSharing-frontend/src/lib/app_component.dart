import 'dart:html';
import 'package:LibreCarSharingFrontend/components/register/register_component.dart';
import 'package:angular2/angular2.dart';
import 'package:angular2/router.dart'; //Routing

// Import components
import 'package:LibreCarSharingFrontend/components/login/login_component.dart';
import 'package:LibreCarSharingFrontend/components/car_display/car_display_component.dart';
import 'package:LibreCarSharingFrontend/components/sidebar/sidebar_component.dart';
import 'package:LibreCarSharingFrontend/models/user.dart';

// Import services
import 'package:LibreCarSharingFrontend/services/car_service.dart';
import 'package:LibreCarSharingFrontend/services/user_service.dart';

// AngularDart info: https://webdev.dartlang.org/angular
// Components info: https://webdev.dartlang.org/components

@Component(
  selector: 'my-app',
  styleUrls: const ['app_component.css'],
  templateUrl: 'app_component.html',
  directives: const [ROUTER_DIRECTIVES, SidebarComponent],
  providers: const [ROUTER_PROVIDERS, CarService, UserService],
)
@RouteConfig(const [
  const Route(path: '/login', name: 'Login', component: LoginComponent),
  const Route(path: '/car/:id', name: 'Car', component: CarDisplayComponent),
  const Route(path: '/register', name: 'Register', component: RegisterComponent)
])
class AppComponent {
  String title = "LibreCarSharing";
  bool debug = false;
  User user;

  final CarService _carService;
  final UserService _userService;

  final Router _router;

  AppComponent(this._carService, this._userService, this._router) {
    //this.user = _userService.getCurrentUser(new Event(null));
    if (this.user == null) {
      _router.navigate(['Login']);
    }
  }
}
