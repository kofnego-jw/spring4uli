import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MessageService} from "./message.service";
import {Subject} from "rxjs/Subject";
import {PictureFW} from "./msg/picture-fw";
import {PictureListMsg} from "./msg/picture-list-msg";
import {PictureFull} from "./msg/picture-full";
import {PictureFullMsg} from "./msg/picture-full-msg";

@Injectable()
export class PictureService implements OnInit{

  public pictureList:Subject<PictureFW[]>;

  public picture:Subject<PictureFull>;

  constructor(private http:HttpClient, private messageService:MessageService) {
    this.pictureList = new Subject();
    this.picture = new Subject();
  }

  ngOnInit(): void {
    this.pictureList.next([]);
    this.loadPictureList();
  }

  loadPictureList():void {
    this.http.get<PictureListMsg>("api/picture")
      .subscribe(msg => {
        this.pictureList.next(msg.pictureList);
        this.messageService.handleMessageResponse(msg);
      }, err => {
        this.messageService.handleErrorResponse(err);
      });
  }

  loadPicture(id:number):void {
    this.http.get<PictureFullMsg>("api/picture/" + id)
      .subscribe(msg => {
        this.messageService.handleMessageResponse(msg);
        this.picture.next(msg.picture);
      }, err => {
        this.messageService.handleErrorResponse(err);
      });
  }
}
