import 'package:angular2/angular2.dart';
import 'dart:convert';
import 'dart:html';

import 'package:LibreCarSharingFrontend/interfaces/community.dart'; // Import community model
import 'package:LibreCarSharingFrontend/implementation/community_impl.dart'; // import community factory

@Injectable()
class CommunityService {
  /** Get all communities
   * @param: id The ID of a user
   **/
  List<Community> getUserCommunities(int id) {
    List<Community> returnList = new List<Community>();
    HttpRequest
        .getString("../api/user/" + id.toString() + "/community")
        .then((String responseText) {
      List response = JSON.decode(responseText);
      for (int i = 0; i < response.length; i++) {
        returnList.add(CommunityImpl.fromJsonString(response.take(i)));
      }
      return returnList;
    }).catchError((n) => print(n));
  }
}
