import 'package:angular2/angular2.dart';
import 'package:angular2/router.dart'; //Routing

// Import components
import 'package:LibreCarSharingFrontend/components/login/login_component.dart';
import 'package:LibreCarSharingFrontend/components/car_display/car_display_component.dart';

// Import services
import 'package:LibreCarSharingFrontend/services/car_service.dart';

// AngularDart info: https://webdev.dartlang.org/angular
// Components info: https://webdev.dartlang.org/components

@Component(
  selector: 'my-app',
  styleUrls: const ['app_component.css'],
  templateUrl: 'app_component.html',
  directives: const [ROUTER_DIRECTIVES],
  providers: const [ROUTER_PROVIDERS, CarService],
  template: '''
  <h1>{{title}}</h1>
  <router-outlet></router-outlet>''',
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
