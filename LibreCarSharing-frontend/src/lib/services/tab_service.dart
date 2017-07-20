import 'dart:async';

import 'package:angular2/angular2.dart';

@Injectable()
class TabService{
  Stream tabStream;
  StreamController _tabStreamController;

  List<String> tabTexts = ["Communities", "Typ"];
  List<String> tabOrderBy = ["communities", "types"];

  TabService() {
    _tabStreamController = new StreamController();
    tabStream = _tabStreamController.stream;
  }

  void activateTab(int i) {
    _tabStreamController.add(tabOrderBy[i]);
  }
}
