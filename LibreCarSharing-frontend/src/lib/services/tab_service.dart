import 'dart:async';

import 'package:angular2/angular2.dart';

@Injectable()
class TabService{
  Stream tabStream;
  StreamController tabStreamController;

  List<String> tabTexts = ["Communities", "Typ"];
  List<String> tabOrderBy = ["communities", "types"];

  TabService() {
    this.tabStreamController = new StreamController();
    this.tabStream = this.tabStreamController.stream;
  }

  void activateTab(int i) {
    this.tabStreamController.add(this.tabOrderBy[i]);
  }
}
