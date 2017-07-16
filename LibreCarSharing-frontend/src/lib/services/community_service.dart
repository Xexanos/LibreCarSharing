import 'package:angular2/angular2.dart';
import 'dart:convert';
import 'dart:html';

import 'package:LibreCarSharingFrontend/models/community.dart'; // Import community model
import 'package:LibreCarSharingFrontend/interfaces/community_impl.dart'; // import community factory

@Injectable()
class CommunityService {
  /** Get all communities
   * @param: id The ID of a community
   **/
  List<Community> getCommunityCars(dynamic e, String userID) {
    e.preventDefault();
    List<Community> returnList = new List<Community>();
    var id = Uri.encodeQueryComponent(userID);
    HttpRequest.postFormData("/rest/api/communitiesfromuser", {"id": id}).then(
            (HttpRequest resp) {
          List response = JSON.decode(resp.responseText);
          for (int i = 0; i < response.length; i++)
            returnList.add(CommunityImpl.fromJsonString(response.take(i)));
        }).catchError((n) => print(n));
    return returnList;
  }
}
