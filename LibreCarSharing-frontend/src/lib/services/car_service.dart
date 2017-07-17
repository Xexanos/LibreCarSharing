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
    HttpRequest.request("/api/community/"+id+"/car", method: "GET" ).then(
        (HttpRequest resp) {
      List response = JSON.decode(resp.responseText);
      for (int i = 0; i < response.length; i++)
        returnList.add(CarImpl.fromJsonString(response.take(i)));
    }).catchError((n) => print(n));
    return returnList;
  }

  /** Get all cars of a certain user
   * @param: id The ID of a user
   **/
  List<Car> getUserCars(dynamic e, String UserID) {
    e.preventDefault();
    List<Car> returnList = new List<Car>();
    var id = Uri.encodeQueryComponent(UserID);
    HttpRequest.request("/api/user/"+id+"/car",method: "GET").then(
        (HttpRequest resp) {
      List response = JSON.decode(resp.responseText);
      for (int i = 0; i < response.length; i++)
        returnList.add(CarImpl.fromJsonString(response.take(i)));
    }).catchError((n) => print(n));
    return returnList;
  }

  getCar(int id) {
    return new Car(
        "Mercedes-Benz Sprinter",
        "https://upload.wikimedia.org/wikipedia/commons/2/2f/Mercedes_sprinter_1_v_sst.jpg",
        "Transporter",
        "Dortmund",
        "DO-BB:22");
  }
}
