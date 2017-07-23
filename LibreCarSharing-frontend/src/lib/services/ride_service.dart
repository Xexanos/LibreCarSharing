import 'package:angular2/angular2.dart';
import 'dart:convert';
import 'dart:html';
import 'dart:async';
import 'package:json_object/json_object.dart';

import 'package:LibreCarSharingFrontend/implementation/ride_impl.dart';
import 'package:LibreCarSharingFrontend/interfaces/ride.dart';

@Injectable()
class RideService {
  /**
   * @param: carId Id of the car
   * @return: all rides associated with a given car
   */
  Future<List<Ride>> getAllRidesFromCar(int carId) {
    Completer completer = new Completer();

    List<Ride> returnList = new List<Ride>();
    HttpRequest
        .getString("../api/car/" + carId.toString() + "/ride")
        .then((String responseText) {
      List<JsonObject> responseList = JSON.decode(responseText);
      //TODO: Das muss auch einfacher gehen!!!
      responseList.forEach((JsonObject jsonObject) {
        returnList.add(new RideImpl.fromJsonString(JSON.encode(jsonObject)));
      });
      object
      completer.complete(returnList);
    }).catchError((n) {
      print("Error in getAllRidesFromCar.");
      completer.complete(null);
    });
    return completer.future;
  }

  Future<Ride> getRide(int rideId) {
    Completer completer = new Completer();

    Ride returnRide;
    HttpRequest.getString("../api/ride/" + rideId.toString()).then((
        String responseText) {
      JsonObject responseObject = JSON.decode(responseText);
      returnRide = new RideImpl.fromJsonString(JSON.encode(responseObject));
      completer.complete(returnRide);
    }).catchError((n) {
      print("Error in getRide.");
      completer.complete(null);
    });
    return completer.future;
  }

  /**
   * Add a new ride to a given car
   * @param: carId id of the car the ride is on
   * @param: ride data to be added
   * @return: the newly created ride
   */
  Future<bool> addRide(int carId, Ride ride) {
    //todo fix
    bool status;

    HttpRequest.request(
        "../api/car/" + carId.toString() + "/ride", method: "POST",
        requestHeaders: {"Content-Type": "application/json"}, sendData: {
      '"name"': '"' + ride.name + '"',
      '"start"': '"' + ride.start.toString() + '"',
      '"end"': '"' + ride.end.toString() + '"'
    }).then((HttpRequest response) {
      if (response.status == 200) {
        status = true;
      } else {
        status = false;
      }
    }).catchError((n) {
      print("Error in addRide.");
      status = false;
    });
    return status;
  }

  /**
   * Modify an existing ride
   * @param: modified ride data
   * @return: the modified ride
   */
  Future<Ride> changeRide(Ride ride) {
    Completer completer = new Completer();

    HttpRequest.request("../api/ride/" + ride.id.toString(),
        method: "PUT",
        requestHeaders: {"Content-Type": "application/json"},
        sendData: {
          '"name"': '"' + ride.name + '"',
          '"start"': '"' + ride.start.toString() + '"',
          '"end"': '"' + ride.end.toString() + '"'
        }).then((HttpRequest response) {
      if (response.status == 200) {
        completer.complete(new RideImpl.fromJsonString(response.responseText));
      } else {
        completer.complete(null);
      }
    }).catchError((Event e) {
      print("Error in changeRide.");
      completer.complete(null);
    });
    return completer.future;
  }

  /**
   * Delete an existing ride
   * @param: rideId id of the ride being deleted
   */
  Future<bool> deleteRide(int rideId) {
    Completer completer = new Completer();
    HttpRequest.request("../api/ride/" + rideId.toString(),
        method: "DELETE").then((HttpRequest response) {
      if (response.status == 200) {
        completer.complete(true);
      } else {
        completer.complete(false);
      }
    }).catchError((Event e) {
      print("Error in deleteRide.");
      completer.complete(null);
    });
    return completer.future;
  }
}
