import 'dart:async';

import 'package:LibreCarSharingFrontend/components/sidebar_part_display/sidebar_part_display_component.dart';

import 'package:LibreCarSharingFrontend/interfaces/part.dart';
import 'package:LibreCarSharingFrontend/services/community_service.dart';
import 'package:LibreCarSharingFrontend/services/tab_service.dart';
import 'package:LibreCarSharingFrontend/services/user_service.dart';
import 'package:angular2/angular2.dart';
import 'package:ng_bootstrap/components/accordion/accordion.dart';


@Component(
  selector: 'sidebarCars',
  styleUrls: const ['sidebar_cars_component.css'],
  templateUrl: 'sidebar_cars_component.html',
  directives: const [
    NG_BOOTSTRAP_ACCORDION_DIRECTIVES, SidebarPartDisplayComponent],
)
class SidebarCarsComponent implements OnInit {
  @Input("orderBy")
  String orderBy;

  List<Part> titles = [];

  final TabService _tabService;
  final CommunityService _communityService;
  final UserService _userService;

  SidebarCarsComponent(this._tabService, this._communityService, this._userService);

  @override
  Future<Null> ngOnInit() async {
    this.setTitles();
    this._tabService.tabStream.listen((String orderBy) {
      this.orderBy = orderBy;
      this.setTitles();
    });
  }

  setTitles() {
    _userService.getCurrentUser().then((user) {
      if (this.orderBy == "types") {
        //this.titles = ["Kleinwagen", "Transporter", "Sportwagen"];
      } else if (this.orderBy == "communities") {
        _communityService.getUserCommunity(user.id).then((List<Part> communities) {
          titles = communities;
        });
      }
    });
    // TODO: implement REST API
  }
}
