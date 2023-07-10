import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../service/project.service';

@Component({
  selector: 'app-confirmmessage',
  templateUrl: './confirmmessage.component.html',
  styleUrls: ['./confirmmessage.component.css']
})
export class ConfirmmessageComponent implements OnInit{
  constructor(private prj:ProjectService){} 
  
  ngOnInit(): void {
    this.prj.closeBoxForProject=false;
  }
  confirmMsg = this.prj.confirmMsg;


  leave(){
  this.prj.closeBoxForProject=true;
  }

  dlt(){
    this.prj.confirmdlt = true;
   }
}
