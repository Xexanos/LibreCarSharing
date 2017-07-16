import 'dart:async';
import 'dart:convert';
import 'dart:html';

import 'package:LibreCarSharingFrontend/interfaces/user_impl.dart';
import 'package:LibreCarSharingFrontend/models/user.dart';
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

  /** try to login given user
   * @param: user The user to login
   */
  void login(dynamic e, User user) {
    e.preventDefault();
    if (debug) {
      this.userStreamController.add(this.getCurrentUser(e));
    } else {
      HttpRequest.postFormData("../login.jsp",
          {"username": user.username, "password": user.password}).then((request) {
        this.userStreamController.add(this.getCurrentUser(e));
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
  User getCurrentUser(dynamic e) {
    e.preventDefault();
    if (debug) {
      User user = new User();
      user.username = "max";
      user.displayName = "Max Mustermann";
      user.email = "max.mustermann@musterdomain.de";
      return user;
    }
  }

  User getUser(int id) {
    if (debug) {
      User user = new User();
      user.username = "max";
      user.displayName = "Max Mustermann";
      user.email = "max.mustermann@musterdomain.de";
      return user;
    }
  }
}