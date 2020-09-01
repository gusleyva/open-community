import { Moment } from 'moment';
import { IProject } from 'app/shared/model/project.model';
import { IVolunteer } from 'app/shared/model/volunteer.model';
import { Language } from 'app/shared/model/enumerations/language.model';

export interface IProjectHistory {
  id?: number;
  startDate?: string;
  endDate?: string;
  language?: Language;
  project?: IProject;
  volunteer?: IVolunteer;
}

export const defaultValue: Readonly<IProjectHistory> = {};
