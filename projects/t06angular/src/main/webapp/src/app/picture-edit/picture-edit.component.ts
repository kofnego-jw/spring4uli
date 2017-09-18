import {Component, OnDestroy, OnInit} from '@angular/core';
import {PictureFull} from "../msg/picture-full";
import {PictureService} from "../picture.service";
import {ActivatedRoute, Params} from "@angular/router";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-picture-edit',
  templateUrl: './picture-edit.component.html',
  styleUrls: ['./picture-edit.component.css']
})
export class PictureEditComponent implements OnInit, OnDestroy {

  picture:PictureFull;

  pictureSubscription:Subscription;

  paramsSubscription:Subscription;

  constructor(private pictureService:PictureService, private activatedRoute:ActivatedRoute) { }

  ngOnInit() {
    this.picture = {
      id:null, path:null, thumb:null, type:null, content:null, projects:[], persons:[]
    };
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => this.handleParams(params));
    this.pictureSubscription = this.pictureService.picture.subscribe(pict => this.picture = pict);
  }

  ngOnDestroy(): void {
    this.paramsSubscription.unsubscribe();
    this.pictureSubscription.unsubscribe();
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

  getContentData():string {
    if (this.picture.content) {
      return "data:" + this.getContentType() + ";base64," + this.picture.content;
    }
    return "data:";
  }

  getThumbData():string {
    if (this.picture.thumb) {
      return "data:" + this.getContentType() + ";base64," + this.picture.thumb;
    }
    return "data:";
  }
}
