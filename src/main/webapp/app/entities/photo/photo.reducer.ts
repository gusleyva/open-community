import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPhoto, defaultValue } from 'app/shared/model/photo.model';

export const ACTION_TYPES = {
  SEARCH_PHOTOS: 'photo/SEARCH_PHOTOS',
  FETCH_PHOTO_LIST: 'photo/FETCH_PHOTO_LIST',
  FETCH_PHOTO: 'photo/FETCH_PHOTO',
  CREATE_PHOTO: 'photo/CREATE_PHOTO',
  UPDATE_PHOTO: 'photo/UPDATE_PHOTO',
  DELETE_PHOTO: 'photo/DELETE_PHOTO',
  SET_BLOB: 'photo/SET_BLOB',
  RESET: 'photo/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPhoto>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type PhotoState = Readonly<typeof initialState>;

// Reducer

export default (state: PhotoState = initialState, action): PhotoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_PHOTOS):
    case REQUEST(ACTION_TYPES.FETCH_PHOTO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PHOTO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PHOTO):
    case REQUEST(ACTION_TYPES.UPDATE_PHOTO):
    case REQUEST(ACTION_TYPES.DELETE_PHOTO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_PHOTOS):
    case FAILURE(ACTION_TYPES.FETCH_PHOTO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PHOTO):
    case FAILURE(ACTION_TYPES.CREATE_PHOTO):
    case FAILURE(ACTION_TYPES.UPDATE_PHOTO):
    case FAILURE(ACTION_TYPES.DELETE_PHOTO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_PHOTOS):
    case SUCCESS(ACTION_TYPES.FETCH_PHOTO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PHOTO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PHOTO):
    case SUCCESS(ACTION_TYPES.UPDATE_PHOTO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PHOTO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType,
        },
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/photos';
const apiSearchUrl = 'api/_search/photos';

// Actions

export const getSearchEntities: ICrudSearchAction<IPhoto> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_PHOTOS,
  payload: axios.get<IPhoto>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<IPhoto> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PHOTO_LIST,
  payload: axios.get<IPhoto>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IPhoto> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PHOTO,
    payload: axios.get<IPhoto>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IPhoto> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PHOTO,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPhoto> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PHOTO,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPhoto> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PHOTO,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType,
  },
});

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
