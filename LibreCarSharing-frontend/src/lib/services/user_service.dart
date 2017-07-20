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
  StreamController _userStreamController;

  User user = null;
  UserService() {
    _userStreamController = new StreamController();
    userStream = _userStreamController.stream;
  }

  /**
   * Get all users
   * @param: id The ID of a community
   **/
  Future<List<User>> getCommunityUsers(int id) {
    Completer completer = new Completer();

    List<User> returnList = new List<User>();
    HttpRequest
        .getString("../api/community/" + id.toString() + "/user")
        .then((String responseText) {
      List response = JSON.decode(responseText);
      for (int i = 0; i < response.length; i++) {
        returnList.add(new UserImpl.fromJsonString(response[i]));
      }
      completer.complete(returnList);
    }).catchError((n) {
      print("Error in getCommunityUsers.");
      completer.complete(null);
    });
    return completer.future;
  }

  /** try to login given user
   * @param: user The user to login
   */
  void login(String username, String password) {
    HttpRequest.postFormData("../login.jsp",
        {"username": username, "password": password}).then((request) {
      getCurrentUser().then((User user) => _userStreamController.add(user));
    }).catchError((n) => print("Error in login."));
  }

  /**
   * logout user
   */
  void logout() {
    HttpRequest.request("../logout", method: "GET").then((request) {
      print(request.getAllResponseHeaders());
      _userStreamController.add(null);
    }).catchError((n) => print("Error in logout."));
  }

  /**
   * @return: user currently logged in
   */
  Future<User> getCurrentUser() {
    Completer completer = new Completer();

    if (user == null) {
      HttpRequest.getString("../api/currentuser").then((String responseText) {
        user = new UserImpl.fromJsonString(responseText);
        completer.complete(user);
      }).catchError((Event e) {
        completer.complete(null);
      });
    } else {
      completer.complete(user);
    }
    return completer.future;
  }
}
