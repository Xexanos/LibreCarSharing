import 'dart:async';

import 'package:LibreCarSharingFrontend/models/community.dart';

import 'package:angular2/angular2.dart';

@Component(
  selector: 'communityDisplay',
  styleUrls: const ['community_display_component.css'],
  templateUrl: 'community_display_component.html',
)
class CommunityDisplayComponent implements OnInit {
  List<Community> communities = [
    new Community(0, "Community 1"), new Community(1, "Community 2")];
  @override
  Future<Null> ngOnInit() async {
    // TODO: implement ngOnInit
  }
}
