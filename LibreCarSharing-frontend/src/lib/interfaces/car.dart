import 'dart:convert';

class Car {
  int id;
  String name;
  String type;
  String location;
  String imageFile;
  bool status;
  String info;
  int seats;
  int color;
  String licencePlate;

  String toJSON() {
    return JSON.encode({'name': name, 'type': type, 'location': location, 'imageFile': imageFile, 'status': status, 'info': info, 'seats': seats, 'color': color, 'licencePlate': licencePlate});
  }
}
