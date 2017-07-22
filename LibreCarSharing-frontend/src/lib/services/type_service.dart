import 'package:angular2/angular2.dart';
import 'dart:convert';
import 'dart:html';
import 'dart:async';

import 'package:LibreCarSharingFrontend/interfaces/type.dart';
import 'package:LibreCarSharingFrontend/implementation/type_impl.dart';
import 'package:json_object/json_object.dart';

@Injectable()
class TypeService {
  /**
   * Get all communities a given user is a member of
   * @param: id The ID of a user
   **/
  Future<List<MyType>> getUserType(int userId) {
    Completer completer = new Completer();

    List<MyType> returnList = new List<TypeImpl>();
    HttpRequest
        .getString("../api/user/" + userId.toString() + "/type")
        .then((String responseText) {
      List<JsonObject> responseList = JSON.decode(responseText);
      //TODO: Das muss auch einfacher gehen!!!
      responseList.forEach((JsonObject jsonObject) {
        returnList.add(new TypeImpl.fromJsonString(JSON.encode(jsonObject)));
      });
      completer.complete(returnList);
    }).catchError((n) {
      print("Error in getUserType.");
      completer.complete(null);
    });
    return completer.future;
  }
}