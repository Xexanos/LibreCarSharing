import 'dart:async';
import 'dart:convert';
import 'dart:html';

import 'package:LibreCarSharingFrontend/implementation/user_impl.dart';
import 'package:LibreCarSharingFrontend/interfaces/user.dart';
import 'package:angular2/angular2.dart';

// Import user model
// import user factory

@Injectable()
class UserService {
  Stream userStream;
  StreamController userStreamController;

  User user = null;

  UserService() {
    this.userStreamController = new StreamController();
    this.userStream = this.userStreamController.stream;
  }

  /** Get all users
   * @param: id The ID of a community
   **/
  List<User> getCommunityUsers(int id) {
    List<User> returnList = new List<User>();
    HttpRequest
        .getString("../api/community/" + id.toString() + "/user")
        .then((String responseText) {
      List response = JSON.decode(responseText);
      for (int i = 0; i < response.length; i++)
        returnList.add(new UserImpl.fromJsonString(response[i]));
    }).catchError((n) => print(n));
    return returnList;
  }

  /** try to login given user
   * @param: user The user to login
   */
  void login(String userName, String password) {
    HttpRequest.postFormData("../login.jsp",
        {"username": userName, "password": password}).then((request) {
      this.userStreamController.add(this.getCurrentUser());
    }).catchError((n) => print(n));
  }

  /** logout user
   *
   */
  void logout() {
    HttpRequest.request("../logout", method: "GET").then((request) {
      print(request.getAllResponseHeaders());
    }).catchError((n) => print(n));
  }

  /**
   * @return: user currently logged in
   */
  User getCurrentUser() {
    if (user == null) {
      HttpRequest.getString("../api/currentuser").then((String responseText) {
        return new UserImpl.fromJsonString(responseText);
      }).catchError((n) => print(n));
    } else {
      return user;
    }
  }
}
