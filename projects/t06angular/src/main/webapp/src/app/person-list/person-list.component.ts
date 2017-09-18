import {Component, OnDestroy, OnInit} from '@angular/core';
import {PersonFW} from "../msg/person-fw";
import {PersonEditingModus, PersonService} from "../person.service";

@Component({
  selector: 'app-person-list',
  templateUrl: './person-list.component.html',
  styleUrls: ['./person-list.component.css']
})
export class PersonListComponent implements OnInit, OnDestroy {

  public personList:PersonFW[];

  private personListSubscription;

  constructor(private personService:PersonService) { }

  ngOnInit() {
    this.personList = [];
    this.personListSubscription = this.personService.personList
      .subscribe(personList => this.personList = personList);
    this.personService.loadPersonList();
    this.personService.setEditingModus(PersonEditingModus.CREATE);
  }

  ngOnDestroy(): void {
    this.personListSubscription.unsubscribe();
  }
}
