import 'dart:async';
import 'package:angular2/angular2.dart';
import 'dart:convert';
import 'dart:html';

import 'package:LibreCarSharingFrontend/interfaces/car.dart';
import 'package:LibreCarSharingFrontend/implementation/car_impl.dart';
import 'package:json_object/json_object.dart';

@Injectable()
class CarService {
  /** Get all cars of a certain community
   * @param: id The ID of a community
   **/
  Future<List<Car>> getCommunityCars(int communityId) {
    Completer completer = new Completer();

    List<Car> returnList = new List<CarImpl>();
    HttpRequest
        .getString("../api/community/" + communityId.toString() + "/car")
        .then((String responseText) {
      List<JsonObject> responseList = JSON.decode(responseText);
      //TODO: Das muss auch einfacher gehen!!!
      responseList.forEach((JsonObject jsonObject) {
        returnList.add(new CarImpl.fromJsonString(JSON.encode(jsonObject)));
      });
      completer.complete(returnList);
    }).catchError((n) {
      print("Error in getCommunityCars.");
      completer.complete(null);
    });
    return completer.future;
  }

  /** Get all cars of a certain type
   * @param: id The ID of a type
   **/
  Future<List<Car>> getTypeCars(int typeId) {
    Completer completer = new Completer();

    List<Car> returnList = new List<CarImpl>();
    HttpRequest
    //TODO: make consistent with getCommunityCars
    //    .getString("../api/type/" + typeId.toString() + "/car")
        .getString("../api/currentuser/car/" + typeId.toString())
        .then((String responseText) {
      List<JsonObject> responseList = JSON.decode(responseText);
      //TODO: Das muss auch einfacher gehen!!!
      responseList.forEach((JsonObject jsonObject) {
        returnList.add(new CarImpl.fromJsonString(JSON.encode(jsonObject)));
      });
      completer.complete(returnList);
    }).catchError((n) {
      print("Error in getTypeCars.");
      completer.complete(null);
    });
    return completer.future;
  }

  /*
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
        returnList.add(new CarImpl.fromJsonString(response[i]));
      }
      return returnList;
    }).catchError((n) => print(n));
  }*/

  /**
   * Get car by id
   */
  Future<Car> getCar(int carId) {
    Completer completer = new Completer();

    HttpRequest
        .getString("../api/car/" + carId.toString())
        .then((String responseText) {
      completer.complete(new CarImpl.fromJsonString(responseText));
    }).catchError((n) {
      print("Error in getCar.");
      completer.complete(null);
    });
    return completer.future;
  }
}
