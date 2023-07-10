import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Project } from 'src/assets/Project';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  
  projectName:any;
  editTask:any;

  projectDetails:any;

  setProjectDetails(project:any){//Sets the column details for work in progress
    this.projectDetails=project;
  }

  projectDetailsTBD:any;
  setProjectDetailsTBD(project:any){
    this.projectDetailsTBD=project;
  }

  setProjectName(name:any){
    this.projectName=name;
  }

  getProjectName(){
    return this.projectName;
  }
// COnfirm message code part 

  confirmMsg:string='';
  confirmdlt?:boolean;

  closeBoxForProject:boolean|any=false;

// ___________CODE for the use in edit project-----------------------------
  editProject:boolean=false;

  private allProjectDetails:any;
  setProjectDetailsForProjectEdit(projectDetails:any){
    this.allProjectDetails=projectDetails;
  }

  getProjectDetailsForProjectEdit(){
    return this.allProjectDetails;
  }

  // ___________CODE for the use in edit project ENDS HERE___________

  constructor(private httpClient:HttpClient) {}
  
  // baseurl:string="http://localhost:8007/api/v1/project/";
  baseurl:string="http://localhost:8085/api/v1/project/";
  
  // -------------------------------



  // -------------------------



  addNewProject(project:any){
    return this.httpClient.post(this.baseurl+"add", project)
  }

  getProject(projectName:any){
    return this.httpClient.get(this.baseurl+projectName);
  }

  updateProject(project:Project){
    return this.httpClient.put(this.baseurl+`save/${project.name}`, project.columns)
  }
  addNewTask(task:any){
    // let name="Corrected Code"
    return this.httpClient.put(this.baseurl+`task/${this.projectName}`, task)
  }

  // _______method for the edit project
    editProjectData(name:any, project:any){
      return this.httpClient.put(this.baseurl+`editProject/${name}`, project)
    }
}