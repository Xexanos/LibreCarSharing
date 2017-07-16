import 'dart:async';
import 'dart:html';

import 'package:angular2/angular2.dart';

import 'package:LibreCarSharingFrontend/models/user.dart';

@Component(
  selector: 'register',
  styleUrls: const ['register_component.css'],
  templateUrl: 'register_component.html',
)
class RegisterComponent implements OnInit {
  User user = new User();

  @override
  Future<Null> ngOnInit() async {
    // TODO: implement ngOnInit
  }

  void register(dynamic e) {
    e.preventDefault();
    HttpRequest.postFormData("/register", {
      "username": this.user.username,
      "displayname": this.user.username,
      "password": this.user.password,
      "email": this.user.email
    })
        .then((request) {})
        .catchError((n) => print(n));
  }
}
