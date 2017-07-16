import 'package:json_object/json_object.dart';

import 'package:LibreCarSharingFrontend/models/user.dart';


class UserImpl extends JsonObject implements User {
  UserImpl();

  /**
   * This factory creates a car object from a JSON string
   */
  factory UserImpl.fromJsonString(String jsonString){
    return new JsonObject.fromJsonString(jsonString, new UserImpl());
  }
}