import { Injectable } from '@angular/core';
import {Subject} from "rxjs/Subject";

@Injectable()
export class MessageService {

  public message:Subject<string>;

  constructor() {
    this.message = new Subject();
  }

  public setMessage(msg:string): void {
    this.message.next(msg);
  }

}
