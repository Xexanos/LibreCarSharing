import 'dart:async';
import 'dart:convert';
import 'dart:html';
import '../../model/car.dart';

import 'package:angular2/angular2.dart';

@Component(
  selector: 'carDisplay',
  styleUrls: const ['car_display_component.css'],
  templateUrl: 'car_display_component.html',
)
class CarDisplayComponent implements OnInit {
  //@Input("car")
  Car car;

  void getcar() {
    HttpRequest.request("../rest/api/carsfromcommunity/1",method: "GET",requestHeaders: {'Accept':'application/json'}).then((response){
      List parsedList = JSON.decode(response.responseText);
      var firstcar =parsedList[0];
      var parsedcar = new Car("not test car");
      parsedcar.name= firstcar["name"];
      window.console.info(parsedList);
      window.console.info(firstcar);
      window.console.info(parsedcar.name);
      //parsedcar.seats= int.parse(firstcar["seats"]);
      this.car = parsedcar;
    }).catchError((n)=>print(n));
  }

  @override
  Future<Null> ngOnInit() async {
    getcar();
  }
}
