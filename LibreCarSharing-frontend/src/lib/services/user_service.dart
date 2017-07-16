import 'package:angular2/angular2.dart';
import 'dart:convert';
import 'dart:html';

import 'package:LibreCarSharingFrontend/models/user.dart'; // Import user model
import 'package:LibreCarSharingFrontend/interfaces/user_impl.dart'; // import user factory

@Injectable()
class UserService {
  /** Get all users
   * @param: id The ID of a community
   **/
  List<User> getCommunityUsers(dynamic e, String communityID) {
    e.preventDefault();
    List<User> returnList = new List<User>();
    var id = Uri.encodeQueryComponent(communityID);
    HttpRequest.postFormData("/rest/api/usersfromcommunity", {"id": id}).then(
            (HttpRequest resp) {
          List response = JSON.decode(resp.responseText);
          for (int i = 0; i < response.length; i++)
            returnList.add(UserImpl.fromJsonString(response.take(i)));
        }).catchError((n) => print(n));
    return returnList;
  }

  User getCurrentUser(dynamic e) {
    e.preventDefault();
    User user = new User();
    user.username = "max";
    user.displayName = "Max Mustermann";
    user.email = "max.mustermann@musterdomain.de";
    return user;
  }
}