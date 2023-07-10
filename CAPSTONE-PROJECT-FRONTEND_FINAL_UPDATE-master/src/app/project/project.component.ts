import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Project } from 'src/assets/Project';
import { UserService } from '../service/user.service';
import { ProjectService } from '../service/project.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ConfirmmessageComponent } from '../confirmmessage/confirmmessage.component';
import { MatDialog } from '@angular/material/dialog';
@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent implements OnInit {

  projectForm: any | FormGroup;

  // projectList: any;

  primary: any = "accent";
  secondary: any = "warn";

  constructor(private dialog: MatDialog, private snackBar: MatSnackBar, private routes: Router, private formBuilder: FormBuilder, private user: UserService, private project: ProjectService, private http: HttpClient) { }

  currentUserName: any;

  projectDetails:any; //This for editing the project

  edit:any;
  ngOnInit() {
    // this.tempArrayForEdit=null;
    
    // this.project.closeBoxForProject=true;

    this.edit=false;


    if( this.project.editProject==true){
      this.projectDetails= this.project.getProjectDetailsForProjectEdit();
      this.edit=true;
      
      
      this.projectForm = this.formBuilder.group({
        name: [ { value: this.getProjectNameForEdit(this.projectDetails.name), disabled: true }, [Validators.required,Validators.pattern(/^[a-zA-Z]/)]],
        members: [this.projectDetails.members],
        memberName: [''],
        columns: [this.getColumnNames()],
        columnName: ['']
      });


    }else{
      this.user.getUser();

    this.projectForm = this.formBuilder.group({
      name: ['', [Validators.required,Validators.pattern(/^[a-zA-Z]/)]],
      members: [[]],
      memberName: [''],
      columns: [[]],
      columnName: ['']
    });

    this.columns.value.push("To Be Done");
    this.columns.value.push("Work In Progress");
    this.columns.value.push("Completed");
    this.members.value.push(this.user.getUser());
    }
    
  }

// Method to get the column names from the edit project details data
getColumnNames() {
  if (this.projectDetails && this.projectDetails.columns) {
    return Object.keys(this.projectDetails.columns);
  }
  return [];
}

getProjectNameForEdit(name:string){
  return name.split("-->")[0];
}

// ____________________________________________________

  addColumn() {
   
    if (!this.columns.value.includes(this.columnName.value.trim()) && this.columnName.value.trim().length > 0) {
      if(this.columns.value.length<6){
        this.columns.value.push(this.columnName.value.trim());
        this.columnName.setValue('');


      }
    } else {
      this.openSnackBar("Empty or Duplicate Columns Not Allowed", "Ok");
    }
  }

  get name() {
    return this.projectForm.get('name');
  }

  get members() {
    return this.projectForm.get('members');
  }

  get memberName() {
    return this.projectForm.get('memberName');
  }

  get columns() {
    return this.projectForm.get('columns')
  }

  get columnName() {
    return this.projectForm.get('columnName')
  }

  findUserName: any;

  tempArrayForEdit:string[]|any=[];

  addMember() {
    this.user.findUserCustomer(this.memberName.value.trim()).subscribe(
      response => {
        this.findUserName = response;
        if (this.findUserName) {

          if (this.members.value.length < 6) {
            if (!this.members.value.includes(this.memberName.value.trim())) {
              this.members.value.push(this.memberName.value.trim());
              this.findUserName = false;

              // --bellow this are the edit condition
              if(this.project.editProject){

                this.tempArrayForEdit.push(this.memberName.value.trim())
                if(this.deletedMember.includes(this.memberName.value.trim())){
                  const index = this.deletedMember.indexOf(this.memberName.value.trim());
                  if (index > -1) {
                    this.deletedMember.splice(index, 1);
                  }
                }
            
              }
              this.memberName.setValue('');

            }else{
              this.openSnackBar("Cannot Add Duplicate or Null members ", "OK")
            }
 
          } else {
            this.openSnackBar("Other than you, Cannot Add more than 5 Employees to a Project", "Ok");
          }
        }else{
          this.openSnackBar("Member do not exist", "")
        }
      },
      error => {
        console.log("This is error" + error);
        this.openSnackBar("Cannot Add Duplicate or Null members ", "OK")
      }
    )
  }

  addProject() {
    //  ---------------------------------------------------------------------------
    if (this.columns.value.length < 2) {
      this.openSnackBar("There must be atleast 2 columns", "Got-It")
    } else {

      const columnList: Map<string, any[]> = new Map();
      for (let i = 0; i < this.columns.value.length; i++) {
        columnList.set(this.columns.value[i], [])
      }

      if (this.projectForm.valid) {
        const project: Project = {
          name: `${this.name.value}-->${this.user.getUser()}`,
          members: this.members.value,
          columns: Object.fromEntries(columnList.entries())
        };


        
        if(this.project.editProject){


          if(this.deletedMember.length>0){
              
            for(let i =0; i<this.deletedMember.length;i++){
          
              this.user.removeProjectOfMember(project.name, this.deletedMember[i]).subscribe(
                response=>{
                  if(response){
                    this.editProjectMethod(project);
                  }
                }                                
              )
            }
           }  else{
              this.editProjectMethod(project);
           }
           
          // ____________________________________NORML WOKRING_________________________________

        }else{
          this.project.addNewProject(project).subscribe(

          response => {
            console.log(response);
            for (let i = 0; i < this.members.value.length; i++) {

              this.http.get(`http://localhost:8085/api/v1/user/updateProject/${this.members.value[i]}/${project.name}`).subscribe(

                response => {console.log(response); 

                  if((i===(this.members.value.length-1)&&response)){
                   
                    this.openSnackBar("Project added Successfuly", "OK");
                    this.routes.navigateByUrl('/', { skipLocationChange: true }).then(() => {
                      this.routes.navigate(['/boardView']);
                    });
                    
                  }
                } );
                
            }
           

          },
          error => {
            this.openSnackBar(`Project with name ${project.name} already exist`, "OK");
          }
        )
      }
        this.routes.navigate(['/boardView'], { state: { ProjectName: project.name } })
      }
    }
  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action,{duration:3000});
  }
  // -----------------------------------------
  boardView(project: string) {
    this.project.setProjectName(project);
    this.routes.navigate(['/boardView']);
  }
  // ----------------------------


  // Delete the members
  deletedMember:string[]|any=[];
  removeMember(member: any) {
    let originalMember:any;
      
      if (this.members.value.includes(member)) {
      let memberIndex = this.members.value.indexOf(member)
      if (memberIndex !== -1) {
        this.members.value.splice(memberIndex, 1);

        
        this.project.getProject(this.projectDetails.name).subscribe(
          response=>{
      
            originalMember=response
            console.log("Member before puhsing to delte array "+originalMember.members);
            if(originalMember.members.includes(member)){
              this.deletedMember.push(member);
              console.log("This is the project details array that is oriinal one +" +this.deletedMember);    
            }
    
            if(this.tempArrayForEdit.includes(member)){
              const index = this.tempArrayForEdit.indexOf(member);
              if (index > -1) {
                this.tempArrayForEdit.splice(index, 1);
              }
            }
          },
            
            error=>{         
              console.log(error);
            }
        )

       
      }

    }
    console.log(this.deletedMember)
  }

  removeColumn(column: any) {

    if (this.columns.value.includes(column) && column !== "To Be Done" && column !== "Work In Progress" && column !== "Completed") {
      let columnIndex = this.columns.value.indexOf(column);
      if (columnIndex !== -1) {
        this.columns.value.splice(columnIndex, 1);
      }
    }
  }

  hideCloseButton(column: any) {

    if (column !== "To Be Done" && column !== "Work In Progress" && column !== "Completed") {
      return true;
    }
    return false;
  }

  hideCloseButtonUser(user: any) {
    if (user == this.user.currentUser) {
      return false
    }
    else {
      return true
    }
  }

  // -----------------Confirm project box close
  dialogOpen:any=this.dialog.open(ConfirmmessageComponent).close();
  confirmWindow() {
    this.project.confirmMsg = "prj";
    this.dialogOpen = this.dialog.open(ConfirmmessageComponent);
  }

  ngDoCheck() {
    if (typeof this.project !== 'undefined' && this.project.closeBoxForProject) {
      this.dialogOpen.close();
    }
  }
  

  editProjectMethod(project:any){
       // ----
       if (this.tempArrayForEdit.length > 0) {
        this.project.editProjectData(this.projectDetails.name, project).subscribe(
          response => {
            let completedRequests = 0; // Counter variable for completed requests
      
            for (let i = 0; i < this.tempArrayForEdit.length; i++) {
              this.http.get(`http://localhost:8085/api/v1/user/updateProject/${this.tempArrayForEdit[i]}/${project.name}`).subscribe(
                response => {
                   
                  if((i===(this.tempArrayForEdit.length-1)&&response)){
                    this.openSnackBar("Project edited Successfully", "OK");
                    this.routes.navigateByUrl('/', { skipLocationChange: true }).then(() => {
                      this.routes.navigate(['/boardView']);
                    });
                  }
                }
              );
            }
          },
          error => {
            console.log(error);
          }
        );
      }
      
      // ---
  }

}

