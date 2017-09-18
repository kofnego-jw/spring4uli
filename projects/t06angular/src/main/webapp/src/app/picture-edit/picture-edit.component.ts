import {Component, ElementRef, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {PictureFull} from "../msg/picture-full";
import {PictureService} from "../picture.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {Subscription} from "rxjs/Subscription";
import {DomSanitizer, SafeHtml} from "@angular/platform-browser";
import {PersonService} from "../person.service";
import {ProjectService} from "../project.service";
import {PersonFW} from "../msg/person-fw";
import {Subject} from "rxjs/Subject";
import {ProjectFW} from "../msg/project-fw";
import {BsModalRef, BsModalService} from "ngx-bootstrap";

@Component({
  selector: 'app-picture-edit',
  templateUrl: './picture-edit.component.html',
  styleUrls: ['./picture-edit.component.css']
})
export class PictureEditComponent implements OnInit, OnDestroy {

  @ViewChild("fileInput") fileInput:ElementRef;

  @ViewChild("personSelector") personSelectorTemplate:TemplateRef<any>;

  personSelectorModal:BsModalRef;

  @ViewChild("projectSelector") projectSelectorTemplate:TemplateRef<any>;

  projectSelectorModal:BsModalRef;

  picture:PictureFull;

  pictureSubscription:Subscription;

  paramsSubscription:Subscription;

  pictureChanged:boolean;

  personList:PersonFW[];

  personListSubscription:Subscription;

  projectList:ProjectFW[];

  projectListSubscription:Subscription;

  constructor(private pictureService:PictureService, private activatedRoute:ActivatedRoute,
              private router:Router, private sanitizer:DomSanitizer,
              private personService:PersonService, private projectService:ProjectService,
              private modalService:BsModalService) { }

  ngOnInit() {
    this.picture = {
      id:null, path:null, thumb:null, type:null, content:null, projects:[], persons:[]
    };
    this.pictureChanged = false;
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => this.handleParams(params));
    this.pictureSubscription = this.pictureService.picture.subscribe(pict => {
      this.picture = pict;
      this.pictureChanged = false;
    });
    this.projectList = [];
    this.projectListSubscription = this.projectService.projectList
      .subscribe(list => this.projectList = list);
    this.projectService.loadProjectList();
    this.personList = [];
    this.personListSubscription = this.personService.personList
      .subscribe(list => this.personList = list);
    this.personService.loadPersonList();
  }

  ngOnDestroy(): void {
    this.paramsSubscription.unsubscribe();
    this.pictureSubscription.unsubscribe();
    this.projectListSubscription.unsubscribe();
    this.personListSubscription.unsubscribe();
  }

  handleParams(params:Params):void {
    if (params['id']) {
      this.pictureService.loadPicture(params['id']);
    }
  }

  getContentType():string {
    if (this.picture.type) {
      switch(this.picture.type) {
        case 'JPEG': return "image/jpeg";
        case 'TIFF': return "image/tiff";
        case 'PNG':  return "image/png";
      }
    }
    return "application/octet-stream";
  }

  getContentData():SafeHtml {
    if (this.picture.content) {
      return this.sanitizer.bypassSecurityTrustResourceUrl(
        "data:" + this.getContentType() + ";base64," + this.picture.content);
    }
    return this.sanitizer.bypassSecurityTrustResourceUrl("");
  }

  getThumbData():SafeHtml {
    if (this.picture.thumb) {
      return this.sanitizer.bypassSecurityTrustResourceUrl(
        "data:" + this.getContentType() + ";base64," + this.picture.thumb);
    }
    return this.sanitizer.bypassSecurityTrustResourceUrl("");
  }

  readFile():void {
    if (!this.fileInput.nativeElement.files) {
      console.log("No files found.");
      console.log(this.fileInput);
      return;
    }
    const files:File[] = this.fileInput.nativeElement.files;
    if (!files.length) {
      console.log("No file found.");
      return;
    }
    const reader = new FileReader();
    reader.onload = () => {
      let result:string = reader.result;
      let pattern:RegExp = /^data:(.*?);base64,(.*)/;
      let matcher = pattern.exec(result);
      this.picture.content = matcher[2];
      let imgType:string = matcher[1];
      if (imgType.indexOf("jpeg") != -1) {
        this.picture.type="JPEG";
      } else if (imgType.indexOf("tiff") != -1) {
        this.picture.type = "TIFF";
      } else if (imgType.indexOf("png") != -1) {
        this.picture.type = "PNG"
      } else {
        this.picture.type = "UNKNOWN";
      }
      this.picture.path = files[0].name;
      this.pictureChanged = true;
    };
    reader.readAsDataURL(files[0]);
  }

  savePicture():void {
    this.picture.thumb=null;
    if (!this.pictureChanged) {
      this.picture.content = null;
    }
    let create:boolean = this.picture.id==null;
    this.pictureService.savePicture(this.picture);
    if (create) {
      this.router.navigateByUrl("/picture/edit");
    }
  }

  deletePicture():void {
    if (this.picture.id) {
      this.pictureService.deletePicture(this.picture.id);
      this.router.navigateByUrl("/picture/list");
    }
  }

  findIndexOfPerson(person:PersonFW):number {
    if (!person || !person.id || !this.picture.persons) {
      return -1;
    }
    for (let i=0; i<this.picture.persons.length; i++) {
      if (this.picture.persons[i].id == person.id) {
        return i;
      }
    }
    return -1;
  }

  togglePerson(person:PersonFW):void {
    const index = this.findIndexOfPerson(person);
    if (index<0) {
      this.picture.persons.push(person);
    } else {
      this.picture.persons.splice(index, 1);
    }
  }

  findIndexOfProject(project:ProjectFW):number {
    if (!project || !project.id || !this.projectList) {
      return -1;
    }
    for (let i=0; i<this.picture.projects.length; i++) {
      if (this.picture.projects[i].id == project.id) {
        return i;
      }
    }
    return -1;
  }

  toggleProject(project:ProjectFW):void {
    const index = this.findIndexOfProject(project);
    if (index<0) {
      this.picture.projects.push(project);
    } else {
      this.picture.projects.splice(index, 1);
    }
  }

  showPersonSelector():void {
    this.personSelectorModal = this.modalService.show(this.personSelectorTemplate);
  }

  showProjectSelector():void {
    this.projectSelectorModal = this.modalService.show(this.projectSelectorTemplate);
  }


}
