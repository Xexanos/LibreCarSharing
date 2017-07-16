import 'dart:async';

import 'package:LibreCarSharingFrontend/components/sidebar_part_display/sidebar_part_display_component.dart';

import 'package:angular2/angular2.dart';

@Component(
  selector: 'sidebar',
  styleUrls: const ['sidebar_component.css'],
  templateUrl: 'sidebar_component.html',
  directives: const [SidebarPartDisplayComponent],
)
class SidebarTypeComponent implements OnInit {
  @Input("kind")
  String kind;

  List<String> titles = [];

  @override
  Future<Null> ngOnInit() async {
    this.titles = ["Kleinwagen", "Transporter", "Sportwagen"];
    // TODO: implement REST API
  }
}
