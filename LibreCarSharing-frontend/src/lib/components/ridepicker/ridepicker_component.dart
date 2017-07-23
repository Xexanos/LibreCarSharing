import 'dart:async';
import 'package:angular2/angular2.dart';
import 'package:ng_bootstrap/components/accordion/accordion.dart';
import 'package:ng_bootstrap/components/datepicker/date_picker.dart';

// Import Interfaces
import 'package:LibreCarSharingFrontend/interfaces/ride.dart';

// Import services
import 'package:LibreCarSharingFrontend/services/ride_service.dart';

@Component(
  selector: 'ridepicker',
  styleUrls: const ['ridepicker_component.css'],
  templateUrl: 'ridepicker_component.html',
  directives: const [
    NG_BOOTSTRAP_ACCORDION_DIRECTIVES,
  ],
)
class RidepickerComponent implements OnInit {
  @Input("carId")
  int carId;

  List<Ride> rides = [];

  final RideService _rideService;

  RidepickerComponent(this._rideService);

  @override
  Future<Null> ngOnInit() async {
    this.getRides();
  }

  getRides() {
    _rideService.getAllRidesFromCar(carId).then((rides) async {
      this.rides = rides;
    });
  }
}
