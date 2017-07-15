import 'dart:async';

import 'package:LibreCarSharingFrontend/model/car.dart';
import 'package:LibreCarSharingFrontend/components/car_display/car_display_component.dart';

import 'package:angular2/angular2.dart';
import 'package:angular_components/angular_components.dart';

@Component(
  selector: 'sidebarPartDisplay',
  styleUrls: const ['sidebar_part_display_component.css'],
  templateUrl: 'sidebar_part_display_component.html',
  directives: const [CarDisplayComponent, GlyphComponent],
)
class SidebarPartDisplayComponent implements OnInit {
  @Input("title")
  String title;

  List<Car> cars;
  bool display = false;

  @override
  Future<Null> ngOnInit() async {}

  toggleDisplay() {
    if (!this.display && this.cars == null) {
      // TODO: implement REST API
      this.cars = [
      ];
    }
    this.display = !this.display;
  }
}
