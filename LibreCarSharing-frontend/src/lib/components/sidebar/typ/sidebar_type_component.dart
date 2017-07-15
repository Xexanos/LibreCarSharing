import 'dart:async';

import 'package:LibreCarSharingFrontend/model/car.dart';
import 'package:LibreCarSharingFrontend/components/car_display/car_display_component.dart';

import 'package:angular2/angular2.dart';

@Component(
  selector: 'sidebarType',
  styleUrls: const ['sidebar_type_component.css'],
  templateUrl: 'sidebar_type_component.html',
    directives: const [CarDisplayComponent],
)
class SidebarTypeComponent implements OnInit {
  List<Car> cars = [];
  List<String> types = [];

  @override
  Future<Null> ngOnInit() async {
    // TODO: implement ngOnInit
  }
}
