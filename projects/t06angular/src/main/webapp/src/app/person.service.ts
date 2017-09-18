import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MessageService} from "./message.service";
import {PersonFW} from "./msg/person-fw";
import {Subject} from "rxjs/Subject";
import {PersonListMsg} from "./msg/person-list-msg";
import {PersonFull} from "./msg/person-full";
import {PersonFullMsg} from "./msg/person-full-msg";
import "rxjs/add/operator/first";

export enum PersonEditingModus {
  CREATE,
  EDIT
}

@Injectable()
export class PersonService implements OnInit {

  public personList:Subject<PersonFW[]>;

  public personInDetail:Subject<PersonFull>;

  public editingModus:Subject<PersonEditingModus>;

  constructor(private http:HttpClient, private messageService:MessageService) {
    this.personList = new Subject();
    this.personInDetail = new Subject();
    this.editingModus = new Subject();
  }


  ngOnInit(): void {
  }

  loadPersonList():void {
    this.http.get<PersonListMsg>("api/person")
      .subscribe(msg => {
        this.messageService.handleMessageResponse(msg);
        this.personList.next(msg.personList);
      }, error => {
        this.messageService.handleErrorResponse(error);
      });
  }

  loadPersonDetail(id:number):void {
    this.http.get<PersonFullMsg>("api/person/" + id)
      .subscribe(msg => {
        this.messageService.handleMessageResponse(msg);
        this.personInDetail.next(msg.person);
      }, error => {
        this.messageService.handleErrorResponse(error);
      });
  }

  createPerson(person:PersonFW):void {
    this.http.post<PersonFullMsg>("api/person", person)
      .subscribe(msg => {
        this.messageService.handleMessageResponse(msg);
        this.personInDetail.next(msg.person);
      }, err => {
        this.messageService.handleErrorResponse(err);
        this.personInDetail.next(new PersonFull());
      });
  }

  updatePerson(person:PersonFW):void {
    const id = person.id;
    if (id===null) {
      this.createPerson(person);
      return;
    }
    this.http.post<PersonFullMsg>("api/person/" + id, person)
      .subscribe(msg => {
        this.messageService.handleMessageResponse(msg);
        this.personInDetail.next(msg.person);
      }, err => {
        this.messageService.handleErrorResponse(err);
        if (person.id) {
          this.loadPersonDetail(person.id);
        }
      });
  }

  deletePerson(id:number):void {
    this.http.delete<PersonListMsg>("api/person/" + id)
      .subscribe(msg => {
        this.messageService.handleMessageResponse(msg);
        this.personList.next(msg.personList);
      }, err => {
        this.messageService.handleErrorResponse(err);
      });
  }

  setEditingModus(modus:PersonEditingModus) {
    this.editingModus.next(modus);
  }
}
