import 'dart:async';

import 'package:LibreCarSharingFrontend/interfaces/ride.dart';
import 'package:LibreCarSharingFrontend/interfaces/community.dart';
import 'package:LibreCarSharingFrontend/interfaces/type.dart';
import 'package:LibreCarSharingFrontend/services/ride_service.dart';
import 'package:LibreCarSharingFrontend/services/type_service.dart';
import 'package:angular2/angular2.dart';
import 'package:angular2/router.dart';
import 'package:angular2/platform/common.dart';

@Component(
  selector: 'rideEdit',
  styleUrls: const ['ride_edit_component.css'],
  templateUrl: 'ride_edit_component.html',
  directives: const [COMMON_DIRECTIVES],
)
class RideEditComponent implements OnInit {
  Ride ride = null;
  List<MyType> types;
  List<Community> communities;
  int carId;
  String color;

  final RouteParams _routeParams;
  final Router _router;
  final Location _location;
  final RideService _rideService;
  final TypeService _typeService;

  RideEditComponent(this._routeParams, this._router, this._rideService,
      this._typeService, this._location);

  @override
  Future<Null> ngOnInit() async {
    var _id = _routeParams.get('id');
    if (_id != null) {
      var id = int.parse(_id ?? '', onError: (_) => null);
      ride = await _rideService.getRide(id);
    } else {
      ride = await _rideService.getRide(-1);
    }
    types = await _typeService.getTypes();
  }

  sendChanges(dynamic e) async {
    e.preventDefault();

    int status = await _rideService.changeRide(ride);
    switch (status) {
      case 200:
        _location.back();
        break;
      default:
        //TODO: Fehler abfangen
        print("Error in sendChanges.");
    }
  }

  deleteRide(dynamic e) async {
    e.preventDefault();

    //TODO: Sicherheitsabfrage
    int status = await _rideService.deleteRide(ride.id);
    print("Error in sendChanges.");
    switch (status) {
      case 200:
        _router.navigate(['Dashboard']);
        break;
      default:
        //TODO: Fehler abfangen
        print("Error in deleteCar.");
    }
  }

  createRide(dynamic e) async {
    e.preventDefault();

    int status = await _rideService.addRide(carId, ride);
    switch (status) {
      case 200:
        _location.back();
        break;
      default:
        //TODO: Fehler abfangen
        print("Error in sendChanges.");
    }
  }
}
