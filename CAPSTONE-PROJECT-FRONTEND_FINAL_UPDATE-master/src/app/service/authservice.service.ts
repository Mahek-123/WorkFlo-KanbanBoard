import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthserviceService {

  constructor() { }

  loginStatus:boolean=false;

  getLoginStatus(){
    if(localStorage.getItem("currentUser")){
      return true;
    }
    return this.loginStatus;
 
  }

  setLoginStatus(){
    this.loginStatus = true;
  }

  setLogOutStatus(){
    localStorage.removeItem("currentUser");
    this.loginStatus = false;
  }

}
