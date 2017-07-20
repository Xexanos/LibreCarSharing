import 'dart:async';

import 'package:LibreCarSharingFrontend/interfaces/community.dart';

import 'package:LibreCarSharingFrontend/services/community_service.dart';
import 'package:angular2/angular2.dart';

@Component(
  selector: 'communityDisplay',
  styleUrls: const ['community_display_component.css'],
  templateUrl: 'community_display_component.html',
)
class CommunityDisplayComponent implements OnInit {
  List<Community> communities;

  final CommunityService _communityService;

  CommunityDisplayComponent(this._communityService);

  @override
  Future<Null> ngOnInit() async {
    this
        ._communityService
        .getCommunities()
        .then((List<Community> communities) => this.communities = communities);
  }
}
