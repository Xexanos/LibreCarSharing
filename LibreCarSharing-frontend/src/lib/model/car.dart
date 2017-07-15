import 'dart:convert';
import 'package:LibreCarSharingFrontend/model/user.dart';

class Car {
  String name = "";
  String typ = "";
  String location = "";
  String imageFile = "";
  bool status = true;
  String information = "";
  User owner = null;
  int seats = 1;
  int color = 0x000000;
  String licencePlate = "";

  Car(this.name, this.imageFile, this.typ, this.location, this.licencePlate);

  String toJSON() {
    return JSON.encode({});
  }
}