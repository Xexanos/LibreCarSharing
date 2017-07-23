import 'dart:async';
import 'package:angular2/angular2.dart';
import 'package:ng_bootstrap/components/accordion/accordion.dart';

import 'package:LibreCarSharingFrontend/components/sidebar_part_display/sidebar_part_display_component.dart';
import 'package:LibreCarSharingFrontend/interfaces/part.dart';
import 'package:LibreCarSharingFrontend/services/community_service.dart';
import 'package:LibreCarSharingFrontend/services/tab_service.dart';
import 'package:LibreCarSharingFrontend/services/type_service.dart';
import 'package:LibreCarSharingFrontend/services/user_service.dart';

@Component(
  selector: 'sidebarCars',
  styleUrls: const ['sidebar_cars_component.css'],
  templateUrl: 'sidebar_cars_component.html',
  directives: const [
    NG_BOOTSTRAP_ACCORDION_DIRECTIVES,
    SidebarPartDisplayComponent
  ],
)
class SidebarCarsComponent implements OnInit {
  @Input("orderBy")
  String orderBy;

  List<Part> titles = [];

  final TabService _tabService;
  final CommunityService _communityService;
  final UserService _userService;
  final TypeService _typeService;

  SidebarCarsComponent(this._tabService, this._communityService,
      this._userService, this._typeService);

  @override
  Future<Null> ngOnInit() async {
    setTitles();
    _tabService.newStream();
    _tabService.tabStream.listen((String orderBy) {
      this.orderBy = orderBy;
      setTitles();
    });
  }

  setTitles() {
    _userService.getCurrentUser().then((user) async {
      if (orderBy == "types") {
        titles = await _typeService.getUserTypes(user.id);
      } else if (orderBy == "communities") {
        titles = await _communityService.getUserCommunities(user.id);
      }
    });
  }
}
