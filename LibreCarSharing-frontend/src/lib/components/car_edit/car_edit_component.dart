import 'dart:async';

import 'package:LibreCarSharingFrontend/interfaces/car.dart';
import 'package:LibreCarSharingFrontend/interfaces/community.dart';
import 'package:LibreCarSharingFrontend/interfaces/type.dart';
import 'package:LibreCarSharingFrontend/services/car_service.dart';
import 'package:LibreCarSharingFrontend/services/community_service.dart';
import 'package:LibreCarSharingFrontend/services/type_service.dart';
import 'package:LibreCarSharingFrontend/services/user_service.dart';
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
  List<Community> communities;
  int communityId;
  String color;

  final RouteParams _routeParams;
  final Router _router;
  final CarService _carService;
  final CommunityService _communityService;
  final TypeService _typeService;
  final UserService _userService;

  CarEditComponent(this._routeParams, this._router, this._carService,
      this._communityService, this._typeService, this._userService);

  @override
  Future<Null> ngOnInit() async {
    var _id = _routeParams.get('id');
    if (_id != null) {
      var id = int.parse(_id ?? '', onError: (_) => null);
      car = await _carService.getCar(id);
      color = "#" + car.color.toRadixString(16);
    } else {
      communities = await _communityService
          .getUserCommunities((await _userService.getCurrentUser()).id);
      car = await _carService.getCar(-1);
      color = "#000000";
    }
    types = await _typeService.getTypes();
  }

  sendChanges(dynamic e) async {
    e.preventDefault();

    car.color = int.parse("0x" + color.substring(1));
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

  createCar(dynamic e) async{
    e.preventDefault();

    int id = await _carService.newCar(communityId, car);
    if (id > 0) {
      _router.navigate(['Car', {'id': id.toString()}]);
    } else {
      //TODO: Fehler abfangen
    }
  }
}
