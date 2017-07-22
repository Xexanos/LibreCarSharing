import 'package:json_object/json_object.dart';

import 'package:LibreCarSharingFrontend/interfaces/type.dart';


class TypeImpl extends JsonObject implements MyType {
  /**
   * This factory creates a car object from a JSON string
   */
  factory TypeImpl.fromJsonString(String jsonString){
    return new JsonObject.fromJsonString(jsonString, new TypeImpl());
  }

  TypeImpl({int id, String name}) {
    this.id = id;
    this.name = name;
  }
}