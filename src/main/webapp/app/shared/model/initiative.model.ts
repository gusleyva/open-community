import { IProject } from 'app/shared/model/project.model';

export interface IInitiative {
  id?: number;
  title?: string;
  projects?: IProject[];
}

export const defaultValue: Readonly<IInitiative> = {};
