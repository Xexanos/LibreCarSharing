import 'dart:async';
import 'package:angular2/angular2.dart';
import 'package:angular2/router.dart';
import 'package:ng_bootstrap/ng_bootstrap.dart';

// Import Interfaces
import 'package:LibreCarSharingFrontend/interfaces/ride.dart';

// Import services
import 'package:LibreCarSharingFrontend/services/ride_service.dart';

@Component(
  selector: 'ridepicker',
  styleUrls: const ['ridepicker_component.css'],
  templateUrl: 'ridepicker_component.html',
  directives: const [
    BS_TABLE_DIRECTIVES,
  ],
    providers: const [ROUTER_PROVIDERS]
)
class RidepickerComponent implements OnInit {
  final RouteParams _routeParams;
  final Router _router;

  @Input("carId")
  int carId;

  int page = 1;

  List<Ride> rides = [];

  final RideService _rideService;

  RidepickerComponent(this._rideService, this._routeParams, this._router);

  @override
  Future<Null> ngOnInit() async {
    this.getRides();
  }

  getRides() {
    _rideService.getAllRidesFromCar(carId).then((rides) async {
      this.rides = rides;
    });
  }

  Future editRide(dynamic e, rideId) =>
      _router.navigate([
        'EditRide',
        {'id': rideId}
      ]);
}
