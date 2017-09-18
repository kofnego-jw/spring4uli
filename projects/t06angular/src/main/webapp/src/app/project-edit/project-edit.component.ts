import {Component, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {ProjectService} from "../project.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {ProjectFull} from "../msg/project-full";
import {Subscription} from "rxjs/Subscription";
import {PersonService} from "../person.service";
import {PersonFW} from "../msg/person-fw";
import {BsModalRef, BsModalService} from "ngx-bootstrap";

@Component({
  selector: 'app-project-edit',
  templateUrl: './project-edit.component.html',
  styleUrls: ['./project-edit.component.css']
})
export class ProjectEditComponent implements OnInit, OnDestroy {

  @ViewChild("projectForm") projectForm:HTMLFormElement;

  @ViewChild("personSelector") personSelectorTemplate:TemplateRef<any>;

  public project:ProjectFull;

  private projectSubscription:Subscription;

  private paramsSubscription:Subscription;

  public personList:PersonFW[];

  private personListSubscription:Subscription;

  public myLeaderId:number;

  public personSelectorModal:BsModalRef;

  public projectChanged:boolean;

  constructor(private projectService:ProjectService, private activatedRoute:ActivatedRoute,
              private personServive:PersonService, private modalService:BsModalService,
              private router:Router) { }

  openPersonSelectorModal():void {
    this.personSelectorModal = this.modalService.show(this.personSelectorTemplate);
  }

  ngOnInit() {
    this.project = {id:null, name:null, goal:null, leader:null, laborators:[]};
    this.projectSubscription = this.projectService.project
      .subscribe(proj => {
        this.project = proj;
        this.myLeaderId = (proj.leader) ? proj.leader.id : 0;
      });
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => this.handleParams(params));
    this.personList=[];
    this.personListSubscription = this.personServive.personList
      .subscribe(list => this.personList = list);
    this.personServive.loadPersonList();
  }

  ngOnDestroy(): void {
    this.projectSubscription.unsubscribe();
    this.paramsSubscription.unsubscribe();
    this.personListSubscription.unsubscribe();
  }

  handleParams(params:Params):void {
    if (params['id']) {
      this.projectService.loadProject(params['id']);
    }
  }

  setProjectLeader() {
    if (!this.project.leader || this.myLeaderId!==this.project.leader.id) {
      this.project.leader = null;
      for (let person of this.personList) {
        if (person.id==this.myLeaderId) {
          this.project.leader = person;
          return;
        }
      }
    }
  }

  findIndexOfLaborator(person:PersonFW):number {
    let personId = person.id;
    if (!personId) return -1;
    for (let i=0; i<this.project.laborators.length; i++) {
      if (personId == this.project.laborators[i].id) {
        return i;
      }
    }
    return -1;
  }

  toggleLaborator(person:PersonFW):void {
    let index = this.findIndexOfLaborator(person);
    if (index>0) {
      this.project.laborators.splice(index, 1);
    } else {
      this.project.laborators.push(person);
    }
    this.projectChanged = true;
  }

  saveProject():void {
    this.projectService.saveProject(this.project);
    if (!this.project.id) {
      this.router.navigateByUrl("/project/edit");
    }
    this.projectChanged = false;
    this.projectForm.reset();
  }

  deleteProject():void {
    if (this.project.id) {
      this.projectService.deleteProject(this.project.id);
      this.router.navigateByUrl("/project/list");
    }
  }

}
