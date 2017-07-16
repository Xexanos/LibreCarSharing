import 'package:angular2/angular2.dart'; //Angular
import 'package:angular2/router.dart'; //Routing

import 'package:ng_bootstrap/ng_bootstrap.dart'; // Bootstrap everything!!!

// Import components
import 'package:LibreCarSharingFrontend/components/login/login_component.dart';
import 'package:LibreCarSharingFrontend/components/car_display/car_display_component.dart';
import 'package:LibreCarSharingFrontend/components/sidebar_cars/sidebar_cars_component.dart';

// Import services
import 'package:LibreCarSharingFrontend/services/car_service.dart';

// AngularDart info: https://webdev.dartlang.org/angular
// Components info: https://webdev.dartlang.org/components

@Component(
  selector: 'my-app',
  styleUrls: const ['app_component.css'],
  templateUrl: 'app_component.html',
  directives: const [BS_DIRECTIVES, ROUTER_DIRECTIVES, SidebarCarsComponent],
  providers: const [ROUTER_PROVIDERS, CarService],
)
@RouteConfig(const[
  const Route(path: '/login', name: 'Login', component: LoginComponent),
  const Route(path: '/car/:id', name: 'Car', component: CarDisplayComponent)
])
class AppComponent{
  String title = "LibreCarSharing";

  final CarService _carService;

  AppComponent(this._carService);
}
