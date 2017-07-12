import 'dart:convert';
import 'user.dart';

class Car {
  String name = "";
  String imageFile = "";
  String location = "";
  String status = "";
  String information = "";
  User owner = null;
  int seats = 1;
  int color = 0x000000;
  String licencePlate = "";

  Car(this.name);

  String toJSON() {
    return JSON.encode({});
  }
}