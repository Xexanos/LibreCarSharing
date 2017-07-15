import 'dart:convert';
import 'package:LibreCarSharingFrontend/model/user.dart';

class Car {
  String name = "";
  String type = "";
  String location = "";
  String imageFile = "";
  bool status = true;
  String information = "";
  User owner = null;
  int seats = 1;
  int color = 0x000000;
  String licencePlate = "";

  Car(this.name, this.imageFile, this.type, this.location, this.licencePlate);

  String toJSON() {
    return JSON.encode({});
  }
}
