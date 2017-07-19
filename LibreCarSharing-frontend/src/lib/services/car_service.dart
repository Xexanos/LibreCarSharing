import 'package:angular2/angular2.dart';
import 'dart:convert';
import 'dart:html';

import 'package:LibreCarSharingFrontend/interfaces/car.dart'; // Import car model
import 'package:LibreCarSharingFrontend/implementation/car_impl.dart'; // import car factory

@Injectable()
class CarService {
  /** Get all cars of a certain community
   * @param: id The ID of a community
   **/
  List<Car> getCommunityCars(dynamic e, String CommunityID) {
    e.preventDefault();
    List<Car> returnList = new List<Car>();
    var id = Uri.encodeQueryComponent(CommunityID);
    HttpRequest
        .getString("../api/community/" + id + "/car")
        .then((String responseText) {
      List response = JSON.decode(responseText);
      for (int i = 0; i < response.length; i++) {
        returnList.add(CarImpl.fromJsonString(response.take(i)));
      }
      return returnList;
    }).catchError((n) => print(n));
  }

  /** Get all cars of a certain user
   * @param: id The ID of a user
   **/
  List<Car> getUserCars(dynamic e, int id) {
    e.preventDefault();
    List<Car> returnList = new List<Car>();
    HttpRequest
        .getString("../api/user/" + id.toString() + "/car")
        .then((String responseText) {
      List response = JSON.decode(responseText);
      for (int i = 0; i < response.length; i++) {
        returnList.add(CarImpl.fromJsonString(response.take(i)));
      }
      return returnList;
    }).catchError((n) => print(n));
  }

  getCar(int id) {
    HttpRequest
        .getString("../api/car/" + id.toString())
        .then((String responseText) {
      return CarImpl.fromJsonString(responseText);
    });
  }
}
