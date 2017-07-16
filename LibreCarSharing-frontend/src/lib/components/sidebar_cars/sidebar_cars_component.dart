import 'dart:async';

import 'package:LibreCarSharingFrontend/components/sidebar_part_display/sidebar_part_display_component.dart';

import 'package:angular2/angular2.dart';

@Component(
  selector: 'sidebarCars',
  styleUrls: const ['sidebar_cars_component.css'],
  templateUrl: 'sidebar_cars_component.html',
  directives: const [SidebarPartDisplayComponent],
)
class SidebarCarsComponent implements OnInit {
  @Input("orderBy")
  String orderBy;

  List<String> titles = [];

  @override
  Future<Null> ngOnInit() async {
    if (this.orderBy == "types") {
      this.titles = ["Kleinwagen", "Transporter", "Sportwagen"];
    } else if (this.orderBy == "communities") {
      this.titles = ["Dortmund", "Bochum", "Essen"];
    }
    // TODO: implement REST API
  }
}
