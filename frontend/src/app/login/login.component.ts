import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  user:{username: String, password: String, stay: boolean} = {username: '', password: '', stay: false};

  constructor() { }

  ngOnInit() {
  }

  login() {
    alert("TODO: login\nUsername ist " + this.user.username + "\nPasswort ist " + this.user.password);
  }
}
