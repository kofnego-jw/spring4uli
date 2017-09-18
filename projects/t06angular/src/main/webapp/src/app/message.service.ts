import { Injectable } from '@angular/core';
import {Subject} from "rxjs/Subject";
import {BaseMsg} from "./msg/base-msg";

@Injectable()
export class MessageService {

  public message:Subject<string>;

  constructor() {
    this.message = new Subject();
  }

  public handleMessageResponse(msg:BaseMsg):void {
    this.setMessage(msg.message);
  }

  public handleErrorResponse(msg:any):void {
    if (msg.message) {
      this.setMessage(msg.message);
      alert(msg.message);
    } else {
      console.log(msg);
      this.setMessage("Error has happend.");
      alert(msg);
    }
  }

  public setMessage(msg:string): void {
    this.message.next(msg);
  }

}
