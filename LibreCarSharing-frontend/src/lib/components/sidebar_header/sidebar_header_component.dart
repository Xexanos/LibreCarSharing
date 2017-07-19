import 'dart:async';

import 'package:LibreCarSharingFrontend/services/tab_service.dart';
import 'package:angular2/angular2.dart';

@Component(
  selector: 'sidebarHeader',
  styleUrls: const ['sidebar_header_component.css'],
  templateUrl: 'sidebar_header_component.html',
)
class SidebarHeaderComponent implements OnInit {
  int activeTab = 0;
  List<String> tabTexts;

  final TabService _tabService;

  SidebarHeaderComponent(this._tabService);

  @override
  Future<Null> ngOnInit() async {
    tabTexts = _tabService.tabTexts;
    // TODO: implement REST API
  }

  setActiveTab(int i) {
    this.activeTab = i;
    _tabService.activateTab(i);
  }
}
