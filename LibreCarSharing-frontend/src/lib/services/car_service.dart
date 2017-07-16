import 'package:angular2/angular2.dart';
import 'dart:convert';
import 'dart:html';

import 'package:LibreCarSharingFrontend/models/car.dart'; // Import car model
import 'package:LibreCarSharingFrontend/interfaces/car_impl.dart'; // import car factory

@Injectable()
class CarService {
  /** Get all cars of a certain community
   * @param: id The ID of a community
   **/
  List<Car> getCommunityCars(dynamic e, String CommunityID) {
    e.preventDefault();
    List<Car> returnList = new List<Car>();
    var id = Uri.encodeQueryComponent(CommunityID);
    HttpRequest.postFormData("/rest/api/carsfromcommunity", {"id": id}).then(
            (HttpRequest resp) {
          List response = JSON.decode(resp.responseText);
          for (int i = 0; i < response.length; i++)
            returnList.add(CarImpl.fromJsonString(response.take(i)));
        }).catchError((n) => print(n));
    return returnList;
  }
}
