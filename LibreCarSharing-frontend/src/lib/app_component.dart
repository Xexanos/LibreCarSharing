import 'package:angular2/angular2.dart';

import 'package:LibreCarSharingFrontend/models/car.dart';
import 'package:LibreCarSharingFrontend/models/user.dart';

import 'package:LibreCarSharingFrontend/components/login/login_component.dart';
import 'package:LibreCarSharingFrontend/components/car_display/car_display_component.dart';
import 'package:LibreCarSharingFrontend/components/community_display/community_display_component.dart';
import 'package:LibreCarSharingFrontend/components/sidebar/sidebar_component.dart';
import 'package:LibreCarSharingFrontend/components/user_display/user_display_component.dart';

// AngularDart info: https://webdev.dartlang.org/angular
// Components info: https://webdev.dartlang.org/components

@Component(
  selector: 'my-app',
  styleUrls: const ['app_component.css'],
  templateUrl: 'app_component.html',
  directives: const [
    LoginComponent,
    CarDisplayComponent,
    CommunityDisplayComponent,
    SidebarComponent,
    UserDisplayComponent
  ],
)
class AppComponent {
  bool debug = true;
  int component = 3;
  String title = "LibreCarSharing";

  Car car = new Car(
      "VW Golf",
      "https://upload.wikimedia.org/wikipedia/commons/6/6f/Golf_2_v2.jpg",
      "Kleinwagen",
      "Dortmund",
      "DO-AA:11");

  User user = new User();
}
