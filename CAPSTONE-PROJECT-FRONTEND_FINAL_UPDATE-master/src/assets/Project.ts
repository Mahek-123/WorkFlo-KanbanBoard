export interface Project{
    name: string;
    members:string[];
    columns:{
        [columnName: string]: Task[];
    };
}

export interface Task {
    name: string;
    content: string;
    priority: string;
    createDate: string;
    deadline: string;
    assignee: string;
    status: string;
    members: string[];
  }