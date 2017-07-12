import 'package:angular2/angular2.dart';

import 'package:frontend/model/car.dart';

import 'package:frontend/components/login/login_component.dart';
import 'package:frontend/components/car_display/car_display_component.dart';
import 'package:frontend/components/community_display/community_display_component.dart';

// AngularDart info: https://webdev.dartlang.org/angular
// Components info: https://webdev.dartlang.org/components

@Component(
  selector: 'my-app',
  styleUrls: const ['app_component.css'],
  templateUrl: 'app_component.html',
  directives: const [LoginComponent, CarDisplayComponent, CommunityDisplayComponent],
)
class AppComponent {
  int component = 1;
  String title = "LibreCarSharing";

  Car car = new Car("VW Golf",
      "https://upload.wikimedia.org/wikipedia/commons/6/6f/Golf_2_v2.jpg",
      "Kleinwagen",
      "Dortmund",
      "DO-AA:11");

}
