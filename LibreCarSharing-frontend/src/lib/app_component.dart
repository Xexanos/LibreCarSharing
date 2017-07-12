import 'package:angular2/angular2.dart';

import 'model/car.dart';

import 'components/login/login_component.dart';
import 'components/car_display/car_display_component.dart';

// AngularDart info: https://webdev.dartlang.org/angular
// Components info: https://webdev.dartlang.org/components

@Component(
  selector: 'my-app',
  styleUrls: const ['app_component.css'],
  templateUrl: 'app_component.html',
  directives: const [LoginComponent, CarDisplayComponent],
)
class AppComponent {
  bool login = true;
  String title = "LibreCarSharing";

  Car car = new Car("Testcar");

}
