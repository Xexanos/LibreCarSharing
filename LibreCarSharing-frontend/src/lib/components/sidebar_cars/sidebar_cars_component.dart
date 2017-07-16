import 'dart:async';

import 'package:LibreCarSharingFrontend/components/sidebar_part_display/sidebar_part_display_component.dart';

import 'package:LibreCarSharingFrontend/services/tab_service.dart';
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

  List<String> titles = [];

  final TabService _tabService;

  SidebarCarsComponent(this._tabService);

  @override
  Future<Null> ngOnInit() async {
    this.setTitles();
    this._tabService.tabStream.listen((String orderBy) {
      this.orderBy = orderBy;
      this.setTitles();
    });
  }

  String setTitles() {
    if (this.orderBy == "types") {
      this.titles = ["Kleinwagen", "Transporter", "Sportwagen"];
    } else if (this.orderBy == "communities") {
      this.titles = ["Dortmund", "Bochum", "Essen"];
    }
    // TODO: implement REST API
  }
}
