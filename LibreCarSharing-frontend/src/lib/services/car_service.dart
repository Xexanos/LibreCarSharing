import 'dart:async';
import 'dart:convert';
import 'dart:html';

import 'package:LibreCarSharingFrontend/implementation/car_impl.dart';
import 'package:LibreCarSharingFrontend/interfaces/car.dart';
import 'package:angular2/angular2.dart';
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
        .getString("../api/type/" + typeId.toString() + "/car")
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

    if (carId == -1) {
      completer.complete(new CarImpl(id: -1));
    } else {
      HttpRequest
          .getString("../api/car/" + carId.toString())
          .then((String responseText) {
        completer.complete(new CarImpl.fromJsonString(responseText));
      }).catchError((n) {
        print("Error in getCar.");
        completer.complete(null);
      });
    }
    return completer.future;
  }

  /**
   * update data of a car inside DB
   * @param: car modified base data
   * @return: statuscode of response
   */
  Future<int> editCar(Car car) {
    Completer completer = new Completer();

    HttpRequest.request("../api/car/" + car.id.toString(),
        method: "PUT",
        requestHeaders: {
          "Content-Type": "application/json"
        },
        sendData: {
          '"name"': '"' + car.name + '"',
          '"licencePlate"': '"' + car.licencePlate + '"',
          '"type"': '"' + car.type + '"',
          '"location"': '"' + car.location + '"',
          '"imageFile"': '"' + car.imageFile + '"',
          '"status"': '"' + car.status.toString() + '"',
          '"info"': '"' + car.info + '"',
          '"seats"': '"' + car.seats.toString() + '"',
          '"color"': '"' + car.color.toString() + '"'
        }).then((HttpRequest response) {
      completer.complete(response.status);
    }).catchError((n) {
      print("Error in editCar.");
      completer.complete(0);
    });
    return completer.future;
  }

  /**
   * create new car
   * @param: car data of new car
   * @return: statuscode of response
   */
  Future<int> newCar(Car car, int communityId) {
    Completer completer = new Completer();

    HttpRequest.request("../api/community/" + communityId.toString() + "/car",
        method: "POST",
        requestHeaders: {
          "Content-Type": "application/json"
        },
        sendData: {
          '"name"': '"' + car.name + '"',
          '"licencePlate"': '"' + car.licencePlate + '"',
          '"type"': '"' + car.type + '"',
          '"location"': '"' + car.location + '"',
          '"imageFile"': '"' + car.imageFile + '"',
          '"status"': '"' + car.status.toString() + '"',
          '"info"': '"' + car.info + '"',
          '"seats"': '"' + car.seats.toString() + '"',
          '"color"': '"' + car.color.toString() + '"'
        }).then((HttpRequest response) {
      completer.complete(response.status);
    }).catchError((n) {
      print("Error in newCar.");
      completer.complete(0);
    });
    return completer.future;
  }

  Future<int> deleteCar(int carId) {
    Completer completer = new Completer();

    HttpRequest
        .request("../api/car/" + carId.toString(), method: "DELETE")
        .then((HttpRequest response) {
      completer.complete(response.status);
    }).catchError((n) {
      print("Error in newCar.");
      completer.complete(0);
    });
    return completer.future;
  }
}
