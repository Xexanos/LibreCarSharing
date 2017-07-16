import 'dart:async';

import 'package:LibreCarSharingFrontend/components/sidebar_header/sidebar_header_component.dart';
import 'package:LibreCarSharingFrontend/components/sidebar_cars/sidebar_cars_component.dart';

import 'package:LibreCarSharingFrontend/models/user.dart';

import 'package:angular2/angular2.dart';

@Component(
  selector: 'sidebar',
  styleUrls: const ['sidebar_component.css'],
  templateUrl: 'sidebar_component.html',
  directives: const [SidebarCarsComponent, SidebarHeaderComponent],
)
class SidebarComponent implements OnInit {
  @Input("user")
  User user;

  @override
  Future<Null> ngOnInit() async {
    // TODO: implement REST API
  }
}
