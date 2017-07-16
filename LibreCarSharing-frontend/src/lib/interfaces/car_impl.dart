import 'package:json_object/json_object.dart';

import 'package:LibreCarSharingFrontend/models/car.dart';


class CarImpl extends JsonObject implements Car {
  CarImpl();

  /**
   * This factory creates a car object from a JSON string
   */
  factory CarImpl.fromJsonString(String jsonString){
    return new JsonObject.fromJsonString(jsonString, new CarImpl());
  }
}