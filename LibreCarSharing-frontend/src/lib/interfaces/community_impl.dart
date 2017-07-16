import 'package:json_object/json_object.dart';

import 'package:LibreCarSharingFrontend/models/community.dart';


class CommunityImpl extends JsonObject implements Community {
  CommunityImpl();

  /**
   * This factory creates a car object from a JSON string
   */
  factory CommunityImpl.fromJsonString(String jsonString){
    return new JsonObject.fromJsonString(jsonString, new CommunityImpl());
  }
}