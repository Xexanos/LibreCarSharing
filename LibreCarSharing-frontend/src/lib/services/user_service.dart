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
  List<User> getCommunityUsers(String communityID) {
    List<User> returnList = new List<User>();
    var id = Uri.encodeQueryComponent(communityID);
    HttpRequest
        .request("../api/community/" + id + "/user", method: "GET")
        .then((HttpRequest resp) {
      List response = JSON.decode(resp.responseText);
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
  void logout(dynamic e) {
    e.preventDefault();
    if (debug) {
      this.userStreamController.add(null);
    } else {
      HttpRequest.request("../logout", method: "GET").then((request) {
        this.userStreamController.add(null);
        print(request.getAllResponseHeaders());
      }).catchError((n) => print(n));
    }
  }

  /**
   * @return: user currently logged in
   */
  User getCurrentUser() {
    if (debug) {
      User user = new UserImpl(
          userName: "max",
          displayName: "Max Mustermann",
          email: "max.mustermann@musterdomain.de");
      return user;
    } else {
      HttpRequest.getString("../api/currentuser").then((String responseText) {
        return UserImpl.fromJsonString(responseText);
      }).catchError((n) => print(n));
    }
  }

  User getUser(int id) {
    if (debug) {
      User user = new UserImpl(
          userName: "max",
          displayName: "Max Mustermann",
          email: "max.mustermann@musterdomain.de");
      return user;
    }
  }
}
