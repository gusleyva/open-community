import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IInitiative, defaultValue } from 'app/shared/model/initiative.model';

export const ACTION_TYPES = {
  SEARCH_INITIATIVES: 'initiative/SEARCH_INITIATIVES',
  FETCH_INITIATIVE_LIST: 'initiative/FETCH_INITIATIVE_LIST',
  FETCH_INITIATIVE: 'initiative/FETCH_INITIATIVE',
  CREATE_INITIATIVE: 'initiative/CREATE_INITIATIVE',
  UPDATE_INITIATIVE: 'initiative/UPDATE_INITIATIVE',
  DELETE_INITIATIVE: 'initiative/DELETE_INITIATIVE',
  RESET: 'initiative/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IInitiative>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type InitiativeState = Readonly<typeof initialState>;

// Reducer

export default (state: InitiativeState = initialState, action): InitiativeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_INITIATIVES):
    case REQUEST(ACTION_TYPES.FETCH_INITIATIVE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_INITIATIVE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_INITIATIVE):
    case REQUEST(ACTION_TYPES.UPDATE_INITIATIVE):
    case REQUEST(ACTION_TYPES.DELETE_INITIATIVE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_INITIATIVES):
    case FAILURE(ACTION_TYPES.FETCH_INITIATIVE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_INITIATIVE):
    case FAILURE(ACTION_TYPES.CREATE_INITIATIVE):
    case FAILURE(ACTION_TYPES.UPDATE_INITIATIVE):
    case FAILURE(ACTION_TYPES.DELETE_INITIATIVE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_INITIATIVES):
    case SUCCESS(ACTION_TYPES.FETCH_INITIATIVE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_INITIATIVE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_INITIATIVE):
    case SUCCESS(ACTION_TYPES.UPDATE_INITIATIVE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_INITIATIVE):
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

const apiUrl = 'api/initiatives';
const apiSearchUrl = 'api/_search/initiatives';

// Actions

export const getSearchEntities: ICrudSearchAction<IInitiative> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_INITIATIVES,
  payload: axios.get<IInitiative>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<IInitiative> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_INITIATIVE_LIST,
  payload: axios.get<IInitiative>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IInitiative> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_INITIATIVE,
    payload: axios.get<IInitiative>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IInitiative> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_INITIATIVE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IInitiative> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_INITIATIVE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IInitiative> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_INITIATIVE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
