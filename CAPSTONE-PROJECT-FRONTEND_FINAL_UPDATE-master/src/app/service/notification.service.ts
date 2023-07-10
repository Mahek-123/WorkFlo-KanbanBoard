import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private httpClient: HttpClient, private user: UserService) { }

  getNotification() {
    return this.httpClient.get('http://localhost:8085/api/v1/notifications/' + localStorage.getItem('currentUser'));
  }
  readAllNotifications(){
    return this.httpClient.get('http://localhost:8085/api/v1/notifications/allRead/' + localStorage.getItem('currentUser'));
  }
  readNotifications(msg:any){
    return this.httpClient.get(`http://localhost:8085/api/v1/notifications/read/${localStorage.getItem('currentUser')}/${msg}`);
  }
}