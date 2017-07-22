import 'dart:async';
import 'package:angular2/angular2.dart';

import 'package:LibreCarSharingFrontend/interfaces/car.dart';

@Component(
  selector: 'sidebarCarDisplay',
  styleUrls: const ['sidebar_car_display_component.css'],
  templateUrl: 'sidebar_car_display_component.html',
)
class SidebarCarDisplayComponent implements OnInit {
  @Input("car")
  Car car;

  @override
  Future<Null> ngOnInit() async {}
}
