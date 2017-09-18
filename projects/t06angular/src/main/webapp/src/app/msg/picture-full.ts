import {PictureFW} from "./picture-fw";
import {PersonFW} from "./person-fw";
import {ProjectFW} from "./project-fw";

export class PictureFull extends PictureFW {

  public type:string;

  public persons:PersonFW[];

  public projects:ProjectFW[];

  public content:string;

  public thumb:string;

}
