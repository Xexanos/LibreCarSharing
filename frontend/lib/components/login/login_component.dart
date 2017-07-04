import 'dart:async';

import 'package:angular2/angular2.dart';

import 'package:frontend/model/user.dart';

@Component(
  selector: 'login',
  styleUrls: const ['login_component.css'],
  templateUrl: 'login_component.html',
)
class LoginComponent implements OnInit {
  User user = new User();

  @override
  Future<Null> ngOnInit() async {
    // TODO: implement ngOnInit
  }

  login() {
    print("TODO: login\nUsername ist " + this.user.username + "\nPasswort ist " + this.user.password);
  }
}
