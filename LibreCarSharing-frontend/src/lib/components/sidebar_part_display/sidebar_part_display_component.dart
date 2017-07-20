import 'dart:async';
import 'package:angular2/angular2.dart';
import 'package:angular2/router.dart';

import 'package:LibreCarSharingFrontend/components/sidebar_car_display/sidebar_car_display_component.dart';
import 'package:LibreCarSharingFrontend/interfaces/car.dart';
import 'package:LibreCarSharingFrontend/interfaces/part.dart';
import 'package:LibreCarSharingFrontend/services/car_service.dart';
import 'package:LibreCarSharingFrontend/services/user_service.dart';

@Component(
  selector: 'sidebarPartDisplay',
  styleUrls: const ['sidebar_part_display_component.css'],
  templateUrl: 'sidebar_part_display_component.html',
  directives: const [SidebarCarDisplayComponent],
)
class SidebarPartDisplayComponent implements OnInit {
  @Input("title")
  Part title;

  @Input("kind")
  String kind;

  List<Car> cars;
  bool display = false;

  final Router _router;
  final UserService _userService;
  final CarService _carService;

  @override
  Future<Null> ngOnInit() async {}

  SidebarPartDisplayComponent(
      this._router, this._userService, this._carService);

  Future toggleDisplay() {
    if (!display && cars == null) {
      _userService.getCurrentUser().then((user) async {
        switch (kind) {
          case "communities":
            cars = await _carService.getCommunityCars(title.id);
            break;
          case "types":
            cars = await _carService
                .getTypeCars(title.id);
            break;
          default:
            cars = null;
        }
      });
    }
    this.display = !this.display;
  }

  void displayCar(int carID) {
    _router.navigate([
      'Car',
      {'id': carID.toString()}
    ]);
  }
}
