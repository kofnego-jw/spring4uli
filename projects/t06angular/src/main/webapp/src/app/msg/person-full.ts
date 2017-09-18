import {PersonFW} from "./person-fw";
import {ProjectFW} from "./project-fw";
import {PictureFW} from "./picture-fw";

export class PersonFull extends PersonFW {

  public asLeader:ProjectFW[];

  public asLaborator:ProjectFW[];

  public inPictures:PictureFW[];

}
