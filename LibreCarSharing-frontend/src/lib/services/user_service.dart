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

    HttpRequest.getString("../api/currentuser").then((String responseText) {
      print(responseText);
      completer.complete(new UserImpl.fromJsonString(responseText));
    }).catchError((Event e) {
      print("Error in getCurrentUser.");
      completer.complete(null);
    });
    return completer.future;
  }

  /**
   * update userdata inside DB
   * @param: user modified base data
   * @param: password to validate user
   * @param: newPassword
   */
  Future<User> changeUser(User user, String password, String newPassword) {
    Completer completer = new Completer();

    if (user.imageFile == null) user.imageFile = "";
    HttpRequest.request("../api/user/" + user.id.toString(),
        method: "PUT",
        requestHeaders: {
          "Content-Type": "application/json"
        },
        sendData: {
          '"username"': '"' + user.username + '"',
          '"password"': '"' + password + '"',
          '"email"': '"' + user.email + '"',
          '"displayName"': '"' + user.displayName + '"',
          '"imageFile"': '"' + user.imageFile + '"',
          '"newPassword"': '"' + newPassword + '"'
        }).then((HttpRequest response) {
      if (response.status == 200) {
        user = new UserImpl.fromJsonString(response.responseText);
        completer.complete(user);
      } else {
        completer.complete(null);
      }
    }).catchError((Event e) {
      print("Error in changeUser.");
      completer.complete(null);
    });
    return completer.future;
  }

  /**
   * register new user
   * @param: username
   * @param password
   * @param: email
   * @return: HTML status code to handle exceptions
   */
  Future<int> registerUser(String username, String password, String email) {
    Completer completer = new Completer();

    HttpRequest.postFormData("/api/user", {
      "username": username,
      "displayName": username,
      "password": password,
      "email": email
    }).then((HttpRequest response) {
      completer.complete(response.status);
    }).catchError((Event e) {
      print("Error in registerUser.");
      completer.complete(0);
    });
    return completer.future;
  }

  Future<int> deleteUser(User user, String password) {
    Completer completer = new Completer();

    HttpRequest.request("/api/user/" + user.id.toString(),
        method: "DELETE",
        requestHeaders: {
          "Content-Type": "application/json"
        },
        sendData: {
          '"username"': '"' + user.username + '"',
          '"password"': '"' + password + '"',
        }).then((HttpRequest response) {
      if (response.status == 200) {
        logout();
      }
      completer.complete(response.status);
    }).catchError((Event e) {
      print("Error in deleteUser.");
      completer.complete(0);
    });
    return completer.future;
  }
}
