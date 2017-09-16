import {Component, OnDestroy, OnInit} from '@angular/core';
import {MessageService} from "../message.service";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit, OnDestroy {

  public message:string;

  private messageSubscription:Subscription;

  constructor(private messageService:MessageService) { }

  ngOnInit() {
    this.message = "Nachrichtensystem initiiert.";
    this.messageSubscription = this.messageService.message
      .subscribe(msg => this.message = msg);
  }

  ngOnDestroy(): void {
    this.messageSubscription.unsubscribe();
  }
}
