import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthserviceService } from '../service/authservice.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from '../service/user.service';
import { ProjectService } from '../service/project.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  constructor(private router:Router ,private authService: AuthserviceService,  
    private userService:UserService,
    private _snackBar: MatSnackBar
    ,private project:ProjectService
    ) { }

  loginForm:any|FormGroup;

  loginStatus:any;

  emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

  hide = true;

  ngOnInit() {
    // alert(`ngOnInit msg ${this.authService.getLoginStatus()}`);
   if(!this.authService.getLoginStatus()){
    this.loginForm = new FormGroup({
      userName: new FormControl('', [Validators.required]),
      password: new FormControl('', Validators.required)
    });
   }else{
    this.loginForm = new FormGroup({
      userName: new FormControl(this.userService.getUser()),
      password: new FormControl('Password')
    });
   }
  }

  ngDoCheck(){
    this.loginStatus = this.authService.getLoginStatus();     
  }

  speak(){
  const speak = new SpeechSynthesisUtterance(`Welcome ${this.loginForm.get('userName').value}`);
    speak.rate=0.8;
    window.speechSynthesis.speak(speak);
  }
  
  type: string = "password";
  isText: boolean = false;
  eyeIcon: string = "fa-eye-slash";

  change() {
    this.isText = !this.isText;
    this.isText ? this.eyeIcon = "fa-eye" : this.eyeIcon = "fa-eye-slash";
    this.isText ? this.type = "text" : this.type = "password";
  }

  responsedata:any;
  loginCustomer() {   
    this.userService.loginUser(this.loginForm.value).subscribe( response=>{
      this.responsedata=response;
      localStorage.setItem("jwt", this.responsedata.Token);

      // this.loginStatus=false;

      this.openSnackBar("Your Login was successful", "Ok") 
      console.log(this.loginForm.get('userName').value);
      localStorage.setItem("currentUser", this.loginForm.get('userName').value)
      
      this.userService.setUser(this.loginForm.get('userName').value); 
      this.authService.setLoginStatus();
      // this.loginStatus = true;
      this.speak();  
      this.router.navigate(['/boardView']);
  
    }, error=> {

      this.openSnackBar("There was error Login Try again", "Ok")  

    })
  }
  logout(){   
  
    this.authService.setLogOutStatus();
    this.userService.resetUser()
    this.loginForm.reset();
    this.project.setProjectName(undefined);
    this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this.router.navigate(['/login']);
  })
}

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action,{duration:1000});
  }

  // Route to register page

  toRegisterPage(){
    this.authService.setLogOutStatus();
    this.userService.resetUser()
    this.loginForm.reset();
    this.project.setProjectName(undefined);
   this.router.navigate(['/register']); 
  }
}
