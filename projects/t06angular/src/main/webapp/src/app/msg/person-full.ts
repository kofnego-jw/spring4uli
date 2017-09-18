import {PersonFW} from "./person-fw";
import {ProjectFW} from "./project-fw";

export class PersonFull extends PersonFW {

  public asLeader:ProjectFW[];

  public asLaborator:ProjectFW[];

}
