import 'dart:async';

import 'package:LibreCarSharingFrontend/implementation/car_impl.dart';
import 'package:LibreCarSharingFrontend/components/sidebar_car_display/sidebar_car_display_component.dart';

import 'package:angular2/angular2.dart';
import 'package:angular2/router.dart';
import 'package:angular_components/angular_components.dart';

@Component(
  selector: 'sidebarPartDisplay',
  styleUrls: const ['sidebar_part_display_component.css'],
  templateUrl: 'sidebar_part_display_component.html',
  directives: const [SidebarCarDisplayComponent, GlyphComponent],
)
class SidebarPartDisplayComponent implements OnInit {
  @Input("title")
  String title;

  List<CarImpl> cars;
  bool display = false;

  final Router _router;

  @override
  Future<Null> ngOnInit() async {}

  SidebarPartDisplayComponent(this._router);

  void toggleDisplay() {
    if (!this.display && this.cars == null) {
      // TODO: implement REST API
      this.cars = [
        new CarImpl(
            name: "VW Golf",
            imageFile: "https://upload.wikimedia.org/wikipedia/commons/6/6f/Golf_2_v2.jpg",
            type: "Kleinwagen",
            location: "Dortmund",
            licencePlate: "DO-AA:11"),
        new CarImpl(
            name: "Mercedes-Benz Sprinter",
            imageFile: "https://upload.wikimedia.org/wikipedia/commons/2/2f/Mercedes_sprinter_1_v_sst.jpg",
            type: "Transporter",
            location: "Dortmund",
            licencePlate: "DO-BB:22")
      ];
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
