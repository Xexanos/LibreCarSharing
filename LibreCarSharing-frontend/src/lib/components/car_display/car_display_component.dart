import 'dart:async';

import 'package:frontend/model/car.dart';

import 'package:angular2/angular2.dart';

@Component(
  selector: 'carDisplay',
  styleUrls: const ['car_display_component.css'],
  templateUrl: 'car_display_component.html',
)
class CarDisplayComponent implements OnInit {
  @Input("car")
  Car car;

  @Input("displayFull")
  bool displayFull;

  @override
  Future<Null> ngOnInit() async {
    // TODO: implement ngOnInit
  }
}