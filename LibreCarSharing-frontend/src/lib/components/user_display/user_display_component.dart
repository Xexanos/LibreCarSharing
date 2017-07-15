import 'dart:async';

import 'package:LibreCarSharingFrontend/model/user.dart';

import 'package:angular2/angular2.dart';

@Component(
  selector: 'userDisplay',
  styleUrls: const ['user_display_component.css'],
  templateUrl: 'user_display_component.html',
)
class UserDisplayComponent implements OnInit {
  @Input("user")
  User user;

  @Input("displayFull")
  bool displayFull;

  @override
  Future<Null> ngOnInit() async {
    // TODO: implement ngOnInit
  }
}