import 'dart:async';

import 'package:LibreCarSharingFrontend/models/car.dart';

import 'package:LibreCarSharingFrontend/services/car_service.dart';
import 'package:angular2/angular2.dart';
import 'package:angular2/router.dart';

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

  final RouteParams _routeParams;
  final CarService _carService;

  CarDisplayComponent(this._routeParams, this._carService);

  @override
  Future<Null> ngOnInit() async {
    var _id = _routeParams.get('id');
    var id = int.parse(_id ?? '', onError: (_) => null);
    if (id != null) car = await (_carService.getCar(id));
  }
}