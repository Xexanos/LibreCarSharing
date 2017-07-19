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

  bool debug = true;

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
        returnList.add(UserImpl.fromJsonString(response.take(i)));
    }).catchError((n) => print(n));
    return returnList;
  }

  /** try to login given user
   * @param: user The user to login
   */
  void login(String userName, String password) {
    if (debug) {
      this.userStreamController.add(this.getCurrentUser());
    } else {
      HttpRequest.postFormData("../login.jsp",
          {"username": userName, "password": password}).then((request) {
        this.userStreamController.add(this.getCurrentUser());
      }).catchError((n) => print(n));
    }
  }

  /** logout user
   *
   */
  void logout() {
    if (debug) {
      this.userStreamController.add(null);
    } else {
      HttpRequest.request("../logout", method: "GET").then((request) {
        print(request.getAllResponseHeaders());
      }).catchError((n) => print(n));
    }
  }

  /**
   * @return: user currently logged in
   */
  User getCurrentUser() {
    HttpRequest.getString("../api/currentuser").then((String responseText) {
      return UserImpl.fromJsonString(responseText);
    }).catchError((n) => print(n));
  }
}
