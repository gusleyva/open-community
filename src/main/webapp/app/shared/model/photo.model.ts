export interface IPhoto {
  id?: number;
  photoContentType?: string;
  photo?: any;
}

export const defaultValue: Readonly<IPhoto> = {};
