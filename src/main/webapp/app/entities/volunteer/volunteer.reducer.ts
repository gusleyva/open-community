import axios from 'axios';
import {
  ICrudSearchAction,
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction,
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IVolunteer, defaultValue } from 'app/shared/model/volunteer.model';

export const ACTION_TYPES = {
  SEARCH_VOLUNTEERS: 'volunteer/SEARCH_VOLUNTEERS',
  FETCH_VOLUNTEER_LIST: 'volunteer/FETCH_VOLUNTEER_LIST',
  FETCH_VOLUNTEER: 'volunteer/FETCH_VOLUNTEER',
  CREATE_VOLUNTEER: 'volunteer/CREATE_VOLUNTEER',
  UPDATE_VOLUNTEER: 'volunteer/UPDATE_VOLUNTEER',
  DELETE_VOLUNTEER: 'volunteer/DELETE_VOLUNTEER',
  RESET: 'volunteer/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IVolunteer>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type VolunteerState = Readonly<typeof initialState>;

// Reducer

export default (state: VolunteerState = initialState, action): VolunteerState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_VOLUNTEERS):
    case REQUEST(ACTION_TYPES.FETCH_VOLUNTEER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_VOLUNTEER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_VOLUNTEER):
    case REQUEST(ACTION_TYPES.UPDATE_VOLUNTEER):
    case REQUEST(ACTION_TYPES.DELETE_VOLUNTEER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_VOLUNTEERS):
    case FAILURE(ACTION_TYPES.FETCH_VOLUNTEER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_VOLUNTEER):
    case FAILURE(ACTION_TYPES.CREATE_VOLUNTEER):
    case FAILURE(ACTION_TYPES.UPDATE_VOLUNTEER):
    case FAILURE(ACTION_TYPES.DELETE_VOLUNTEER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_VOLUNTEERS):
    case SUCCESS(ACTION_TYPES.FETCH_VOLUNTEER_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_VOLUNTEER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_VOLUNTEER):
    case SUCCESS(ACTION_TYPES.UPDATE_VOLUNTEER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_VOLUNTEER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/volunteers';
const apiSearchUrl = 'api/_search/volunteers';

// Actions

export const getSearchEntities: ICrudSearchAction<IVolunteer> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_VOLUNTEERS,
  payload: axios.get<IVolunteer>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IVolunteer> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_VOLUNTEER_LIST,
    payload: axios.get<IVolunteer>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IVolunteer> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_VOLUNTEER,
    payload: axios.get<IVolunteer>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IVolunteer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_VOLUNTEER,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<IVolunteer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_VOLUNTEER,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IVolunteer> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_VOLUNTEER,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
