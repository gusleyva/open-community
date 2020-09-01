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

import { IProjectHistory, defaultValue } from 'app/shared/model/project-history.model';

export const ACTION_TYPES = {
  SEARCH_PROJECTHISTORIES: 'projectHistory/SEARCH_PROJECTHISTORIES',
  FETCH_PROJECTHISTORY_LIST: 'projectHistory/FETCH_PROJECTHISTORY_LIST',
  FETCH_PROJECTHISTORY: 'projectHistory/FETCH_PROJECTHISTORY',
  CREATE_PROJECTHISTORY: 'projectHistory/CREATE_PROJECTHISTORY',
  UPDATE_PROJECTHISTORY: 'projectHistory/UPDATE_PROJECTHISTORY',
  DELETE_PROJECTHISTORY: 'projectHistory/DELETE_PROJECTHISTORY',
  RESET: 'projectHistory/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProjectHistory>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ProjectHistoryState = Readonly<typeof initialState>;

// Reducer

export default (state: ProjectHistoryState = initialState, action): ProjectHistoryState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_PROJECTHISTORIES):
    case REQUEST(ACTION_TYPES.FETCH_PROJECTHISTORY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PROJECTHISTORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PROJECTHISTORY):
    case REQUEST(ACTION_TYPES.UPDATE_PROJECTHISTORY):
    case REQUEST(ACTION_TYPES.DELETE_PROJECTHISTORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_PROJECTHISTORIES):
    case FAILURE(ACTION_TYPES.FETCH_PROJECTHISTORY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PROJECTHISTORY):
    case FAILURE(ACTION_TYPES.CREATE_PROJECTHISTORY):
    case FAILURE(ACTION_TYPES.UPDATE_PROJECTHISTORY):
    case FAILURE(ACTION_TYPES.DELETE_PROJECTHISTORY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_PROJECTHISTORIES):
    case SUCCESS(ACTION_TYPES.FETCH_PROJECTHISTORY_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_PROJECTHISTORY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PROJECTHISTORY):
    case SUCCESS(ACTION_TYPES.UPDATE_PROJECTHISTORY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PROJECTHISTORY):
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

const apiUrl = 'api/project-histories';
const apiSearchUrl = 'api/_search/project-histories';

// Actions

export const getSearchEntities: ICrudSearchAction<IProjectHistory> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_PROJECTHISTORIES,
  payload: axios.get<IProjectHistory>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IProjectHistory> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PROJECTHISTORY_LIST,
    payload: axios.get<IProjectHistory>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IProjectHistory> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PROJECTHISTORY,
    payload: axios.get<IProjectHistory>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IProjectHistory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PROJECTHISTORY,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<IProjectHistory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PROJECTHISTORY,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProjectHistory> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PROJECTHISTORY,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
