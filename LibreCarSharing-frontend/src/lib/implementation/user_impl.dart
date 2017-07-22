import 'package:json_object/json_object.dart';

import 'package:LibreCarSharingFrontend/interfaces/user.dart';


class UserImpl extends JsonObject implements User {

  /**
   * This factory creates a car object from a JSON string
   */
  factory UserImpl.fromJsonString(String jsonString){
    return new JsonObject.fromJsonString(jsonString, new UserImpl());
  }

  UserImpl({int id, String username, String displayName, String email, String imageFile}) {
    this.id = id;
    this.username = username;
    this.displayName = displayName;
    this.email = email;
    this.imageFile = imageFile;
  }
}