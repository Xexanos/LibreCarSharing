import 'dart:async';

import 'package:LibreCarSharingFrontend/model/community.dart';

import 'package:angular2/angular2.dart';

@Component(
  selector: 'communityDisplay',
  styleUrls: const ['community_display_component.css'],
  templateUrl: 'community_display_component.html',
)
class CommunityDisplayComponent implements OnInit {
  List<community> communities = [new community("Community 1"), new community("Community 2")];
  @override
  Future<Null> ngOnInit() async {
    // TODO: implement ngOnInit
  }
}
