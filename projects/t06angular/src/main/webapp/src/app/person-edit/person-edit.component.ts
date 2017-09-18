import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {PersonFull} from "../msg/person-full";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {PersonEditingModus, PersonService} from "../person.service";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-person-edit',
  templateUrl: './person-edit.component.html',
  styleUrls: ['./person-edit.component.css']
})
export class PersonEditComponent implements OnInit, OnDestroy {

  @ViewChild("personForm") personForm:HTMLFormElement;

  public person:PersonFull;

  private personSubscription:Subscription;

  private paramsSubscription:Subscription;

  constructor(private activatedRoute:ActivatedRoute, private personService:PersonService, private router:Router) { }

  ngOnInit() {
    this.person = {id:null, name:null, email:null, info:null, asLaborator:[], asLeader:[]};
    this.personSubscription = this.personService.personInDetail.subscribe(
      person => {
        this.person = person;
      }
    );
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => this.handleParams(params));
  }

  ngOnDestroy(): void {
    this.personSubscription.unsubscribe();
    this.paramsSubscription.unsubscribe();
  }

  handleParams(params:Params):void {
    let id = params['id'];
    if (id) {
      this.personService.loadPersonDetail(id);
      this.personService.setEditingModus(PersonEditingModus.EDIT);
    }
  }

  savePerson() {
    if (this.person.id) {
      this.personService.updatePerson(this.person);
    } else {
      this.personService.createPerson(this.person);
      this.router.navigateByUrl("/person/edit");
    }
    this.personForm.reset();
  }

  deletePerson() {
    if (this.person.id) {
      this.personService.deletePerson(this.person.id);
      this.router.navigateByUrl("/person/list");
    }
  }
}
