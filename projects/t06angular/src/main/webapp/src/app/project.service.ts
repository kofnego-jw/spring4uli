import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MessageService} from "./message.service";
import {Subject} from "rxjs/Subject";
import {ProjectFW} from "./msg/project-fw";
import {ProjectFull} from "./msg/project-full";
import {ProjectListMsg} from "./msg/project-list-msg";
import {ProjectFullMsg} from "./msg/project-full-msg";

@Injectable()
export class ProjectService implements OnInit{

  public projectList:Subject<ProjectFW[]>;

  public project:Subject<ProjectFull>;

  constructor(private http:HttpClient, private messageService:MessageService) {
    this.project = new Subject();
    this.projectList = new Subject();
  }

  ngOnInit(): void {
    this.projectList.next([]);
    this.loadProjectList();
  }

  loadProjectList():void {
    this.http.get<ProjectListMsg>("api/project")
      .subscribe(msg => {
        this.messageService.handleMessageResponse(msg);
        this.projectList.next(msg.projectList);
      }, err => {
        this.messageService.handleErrorResponse(err);
      });
  }

  loadProject(id:number):void {
    this.http.get<ProjectFullMsg>("api/project/" + id)
      .subscribe(msg => {
        this.project.next(msg.project);
        this.messageService.handleMessageResponse(msg);
      }, err => {
        this.messageService.handleErrorResponse(err);
      });
  }

  saveProject(proj:ProjectFull):void {
    if (proj.id) {
      this.updateProject(proj);
    } else {
      this.createProject(proj);
    }
  }

  createProject(proj:ProjectFull):void {
    this.http.post<ProjectFullMsg>("api/project", proj)
      .subscribe(msg => {
        this.messageService.handleMessageResponse(msg);
        this.project.next(msg.project);
      }, err => {
        this.messageService.handleErrorResponse(err);
      });
  }

  updateProject(proj:ProjectFull):void {
    const id = proj.id;
    if (!id) {
      this.createProject(proj);
      return;
    }
    this.http.post<ProjectFullMsg>("api/project/" + id, proj)
      .subscribe(msg => {
        this.messageService.handleMessageResponse(msg);
        this.project.next(msg.project);
      }, err => {
        this.messageService.handleErrorResponse(err);
      });
  }

  deleteProject(id:number):void {
    this.http.delete<ProjectListMsg>("api/project/" + id)
      .subscribe(msg => {
        this.projectList.next(msg.projectList);
        this.messageService.handleMessageResponse(msg);
      }, err => {
        this.messageService.handleErrorResponse(err);
      });
  }
}
