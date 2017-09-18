import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProjectFW} from "../msg/project-fw";
import {ProjectService} from "../project.service";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css']
})
export class ProjectListComponent implements OnInit, OnDestroy {

  public projectList:ProjectFW[];

  private projectListSubscription:Subscription;

  constructor(private projectService:ProjectService) { }

  ngOnInit() {
    this.projectList = [];
    this.projectListSubscription = this.projectService.projectList
      .subscribe(list => this.projectList = list);
    this.projectService.loadProjectList();
  }

  ngOnDestroy(): void {
    this.projectListSubscription.unsubscribe();
  }
}
