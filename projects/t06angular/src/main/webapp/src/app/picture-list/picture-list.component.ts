import {Component, OnDestroy, OnInit} from '@angular/core';
import {PictureFW} from "../msg/picture-fw";
import {Subscription} from "rxjs/Subscription";
import {PictureService} from "../picture.service";

@Component({
  selector: 'app-picture-list',
  templateUrl: './picture-list.component.html',
  styleUrls: ['./picture-list.component.css']
})
export class PictureListComponent implements OnInit, OnDestroy {

  public pictureList:PictureFW[];

  private picutreListSubscription:Subscription;

  constructor(private pictureService:PictureService) { }

  ngOnInit() {
    this.pictureList = [];
    this.picutreListSubscription = this.pictureService.pictureList
      .subscribe(list => this.pictureList = list);
    this.pictureService.loadPictureList();
  }


  ngOnDestroy(): void {
    this.picutreListSubscription.unsubscribe();
  }
}
