import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ProjectService } from '../service/project.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CustomValidation } from '../service/CustomValidation';

@Component({
  selector: 'app-task',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.css']
})
export class TaskComponent implements OnInit {

  priorityColor: any;

  projectMembers: string[] = [];

  project: any;

  constructor(private fb: FormBuilder, private routing: Router, private projectService: ProjectService, private snackBar:MatSnackBar ) { }
  ngOnInit(): void {
    this.setPriority("Clear");
    this.projectService.getProject(this.projectService.projectName).subscribe(data => {
       this.project = data; this.projectMembers = this.project.members 
      });
      this.createDate=this.currentDate;
  }
  membersList: string[] = [];

  public currentDate: Date = new Date();
  createDate: any;
  createDate1: any;
  deadline: any;
  user: any = localStorage.getItem("currentUser");


  setDate() {
    console.log(this.startDate!.value)
    this.createDate = this.startDate?.value
    console.log(typeof (this.createDate));

    let hoursDiff = this.createDate.getHours() - this.createDate.getTimezoneOffset() / 60;
    let minutesDiff = (this.createDate.getHours() - this.createDate.getTimezoneOffset()) % 60;
    this.createDate.setHours(hoursDiff);
    this.createDate.setMinutes(minutesDiff);

  }

fetchedProjectDetails:any;
frequencyMethod(){
  if (this.projectService.projectDetails.length==0){
    return this.projectMembers;
  }
  if (!this.projectService.projectDetails == null || typeof this.projectService.projectDetails !== "undefined") {

       this.fetchedProjectDetails=this.projectService.projectDetails;
        let memberArray:any=[]; 
      for(let i=0; i<this.fetchedProjectDetails.length;i++){
        for(let j=0; j<this.fetchedProjectDetails[i].members.length;j++){
          if(this.projectMembers.includes(this.fetchedProjectDetails[i].members[j])){
            if(this.fetchedProjectDetails[i]?.status!=="Archived"){
              memberArray.push(this.fetchedProjectDetails[i]?.members[j]);
            }
          }

        }
      }

      for (let i = 0; i < this.projectMembers.length; i++) {
        if (!memberArray.includes(this.projectMembers[i])&&this.projectMembers.includes(this.projectMembers[i])) {
          if(this.fetchedProjectDetails[i]?.status!=="Archive"){
            memberArray.push(this.projectMembers[i]);
          }
          
        }
      }

      let occurrenceOfMembers:any ={};

      for(let i=0;i<memberArray.length;i++){
        let memberName=memberArray[i]

        if(occurrenceOfMembers[memberName]){
          occurrenceOfMembers[memberName]++;
        }else{
          occurrenceOfMembers[memberName]=1
        }
      }
      
      let availableMembersArray:any=[];
      for(let key in occurrenceOfMembers){
        if(occurrenceOfMembers[key]<5){
          availableMembersArray.push(key)
        }
      }

      return availableMembersArray;
   }

}


  AddTask = this.fb.group({
    taskName: ['', [Validators.required,Validators.pattern( /^[a-zA-Z]/),  CustomValidation.titleValidator]],
    taskContent: ['', [Validators.required, CustomValidation.contentValidator]],
    taskPriority: [''],
    startDate: [''],
    dueDate: [''],
    members: [[]]
  })

  get taskName() { return this.AddTask.get("taskName") }

  get taskContent() { return this.AddTask.get("taskContent") }

  get taskPriority() { return this.AddTask.get("taskPriority") }

  get startDate() { return this.AddTask.get("startDate") }

  get dueDate() { return this.AddTask.get("dueDate") }

  get members() { return this.AddTask.get("members") }

  setPriority(colorCode: any) {
    this.priorityColor = colorCode;
  }

  onSubmit() {
    this.deadline = this.dueDate?.value

    if(this.AddTask.get('dueDate')?.dirty){
      let hoursDiff = this.deadline.getHours() - this.deadline.getTimezoneOffset() / 60;
      let minutesDiff = (this.deadline.getHours() - this.deadline.getTimezoneOffset()) % 60;
      this.deadline.setHours(hoursDiff);
      this.deadline.setMinutes(minutesDiff);
    }
    
    const task: any = {
      name: this.taskName?.value,
      content: this.taskContent?.value,
      priority: this.priorityColor,
      createDate: this.createDate,
      deadline: this.deadline,
      assignee: this.user,
      status:'Not Archived',
      members: this.members?.value
    };

    if(this.projectService.projectDetailsTBD.length<10){
      this.projectService.addNewTask(task).subscribe(
        repsonse => {
          console.log(repsonse)
        },
        error => {
          console.log(error)
        this.openSnackBar("Cannot add task with the same name ", "OK")
        }
      );
  
    }else{
      this.openSnackBar("Cannot Add More than 10 task in To-BE-Done", "OK")
    }
    this.onClose()
  }

  onClose() {
    this.routing.navigateByUrl('/', { skipLocationChange: true }).then(() => {
    this.routing.navigate(['/boardView']);
    });
  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action);
  }
}