import { ILocation } from 'app/shared/model/location.model';
import { IProject } from 'app/shared/model/project.model';

export interface IVolunteer {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string;
  age?: number;
  location?: ILocation;
  projects?: IProject[];
  coordinator?: IVolunteer;
}

export const defaultValue: Readonly<IVolunteer> = {};
