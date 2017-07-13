import 'dart:async';

import 'package:frontend/model/car.dart';

import 'package:angular2/angular2.dart';

@Component(
  selector: 'sidebarType',
  styleUrls: const ['sidebar_type_component.css'],
  templateUrl: 'sidebar_type_component.html',
)
class SidebarTypeComponent implements OnInit {
  List<Car> cars = [];
  List<String> types = [];

  @override
  Future<Null> ngOnInit() async {
    // TODO: implement ngOnInit
  }
}
