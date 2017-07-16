import 'user.dart';
import 'dart:convert';

class Car {
  int id = 0;
  String name = "";
  String type = "";
  String location = "";
  String imageFile = "";
  bool status = true;
  String info = "";
  User owner = null;
  int seats = 1;
  int color = 0x000000;
  String licencePlate = "";

  Car(this.name, this.imageFile, this.type, this.location, this.licencePlate);

  String toJSON() {
    return JSON.encode({});
  }
}