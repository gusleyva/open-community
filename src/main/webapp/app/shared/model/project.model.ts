import { Moment } from 'moment';
import { ILocation } from 'app/shared/model/location.model';
import { IPhoto } from 'app/shared/model/photo.model';
import { IInitiative } from 'app/shared/model/initiative.model';
import { IVolunteer } from 'app/shared/model/volunteer.model';

export interface IProject {
  id?: number;
  projectTitle?: string;
  startDate?: string;
  endDate?: string;
  registrationDeadLine?: string;
  description?: string;
  volunteerTask?: string;
  maxVolunteer?: number;
  minAge?: number;
  maxGuest?: number;
  volunteerInstructions?: string;
  additionalProjectDetails?: string;
  location?: ILocation;
  photo?: IPhoto;
  initiatives?: IInitiative[];
  volunteer?: IVolunteer;
}

export const defaultValue: Readonly<IProject> = {};
