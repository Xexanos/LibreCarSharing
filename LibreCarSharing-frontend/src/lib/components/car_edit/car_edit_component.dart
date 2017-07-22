import 'dart:async';

import 'package:LibreCarSharingFrontend/interfaces/car.dart';

import 'package:LibreCarSharingFrontend/interfaces/type.dart';
import 'package:LibreCarSharingFrontend/services/car_service.dart';
import 'package:LibreCarSharingFrontend/services/type_service.dart';
import 'package:angular2/angular2.dart';
import 'package:angular2/router.dart';

@Component(
  selector: 'carEdit',
  styleUrls: const ['car_edit_component.css'],
  templateUrl: 'car_edit_component.html',
  directives: const [COMMON_DIRECTIVES],
)
class CarEditComponent implements OnInit {
  Car car = null;
  List<MyType> types;

  final RouteParams _routeParams;
  final Router _router;
  final CarService _carService;
  final TypeService _typeService;

  CarEditComponent(this._routeParams, this._router, this._carService, this._typeService);

  @override
  Future<Null> ngOnInit() async {
    var _id = _routeParams.get('id');
    var id = int.parse(_id ?? '', onError: (_) => null);
    if (id != null) car = await _carService.getCar(id);
    types = await _typeService.getTypes();
  }

  sendChanges(dynamic e) async {
    e.preventDefault();

    print(car.color);
    int status = await _carService.editCar(car);
    switch (status) {
      case 200:
        _router.navigate([
          'Car',
          {'id': car.id.toString()}
        ]);
        break;
      default:
        print("Error in sendChanges.");
    }
  }

  deleteCar(dynamic e) async {
    e.preventDefault();

    //TODO: Sicherheitsabfrage
    int status = await _carService.deleteCar(car.id);
    switch (status) {
      case 200:
        _router.navigate(['Dashboard']);
        break;
      default:
        print("Error in deleteCar.");
    }
  }
}
