import 'package:json_object/json_object.dart';

import 'package:LibreCarSharingFrontend/interfaces/car.dart';

class CarImpl extends JsonObject implements Car {
  /**
   * This factory creates a car object from a JSON string
   */
  factory CarImpl.fromJsonString(String jsonString) {
    return new JsonObject.fromJsonString(jsonString, new CarImpl());
  }

  CarImpl(
      {int id,
      String name,
      String type,
      String location,
      String imageFile,
      bool status,
      String info,
      int seats,
      int color,
      String licencePlate}) {
    this.id = id;
  }
}
