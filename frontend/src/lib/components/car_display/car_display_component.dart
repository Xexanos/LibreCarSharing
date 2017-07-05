import 'dart:async';

import '../../model/car.dart';

import 'package:angular2/angular2.dart';

@Component(
  selector: 'carDisplay',
  styleUrls: const ['car_display_component.css'],
  templateUrl: 'car_display_component.html',
)
class CarDisplayComponent implements OnInit {
  @Input("car")
  Car car;

  @override
  Future<Null> ngOnInit() async {
    // TODO: implement ngOnInit
  }
}
