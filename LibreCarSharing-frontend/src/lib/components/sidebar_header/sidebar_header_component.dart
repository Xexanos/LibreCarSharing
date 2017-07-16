import 'dart:async';

import 'package:angular2/angular2.dart';

@Component(
  selector: 'sidebarHeader',
  styleUrls: const ['sidebar_header_component.css'],
  templateUrl: 'sidebar_header_component.html',
)
class SidebarHeaderComponent implements OnInit {
  int activeTab = 0;
  List<String> tabTexts = ["Communities", "Typ"];
  List<String> tabOrderBy = ["communities", "types"];

  @override
  Future<Null> ngOnInit() async {
    // TODO: implement REST API
  }

  setActiveTab(int i) {
    this.activeTab = i;
    //TODO: reflect change within displayed cars
  }
}
