import 'package:json_object/json_object.dart';

import 'package:LibreCarSharingFrontend/interfaces/community.dart';


class CommunityImpl extends JsonObject implements Community {
  /**
   * This factory creates a car object from a JSON string
   */
  factory CommunityImpl.fromJsonString(String jsonString){
    return new JsonObject.fromJsonString(jsonString, new CommunityImpl());
  }

  CommunityImpl({int id, String name}) {
    this.id = id;
    this.name = name;
  }
}