import services from '../services/crawl';
import servicesList from '../services/list';

export default {
    namespace : 'crawl',
    state : {
        cityData: [],
        selectedCity: null,
        progress: null,
        hotelMount: 0,
        loading: false,
        showProgress: false,
    },
    effects: {
        *handleSearch({payload}, {put, call}) {

            const res = yield call(services.start, payload);
            yield put({
                type: 'searched',
            })
        },

        *searchProgress({payload}, {put, call}) {
            const res = yield call(services.getProgress, payload);
            yield put({
                type: 'gotProgress',
                payload: res.data,
            })
        },

        * fetchCity({payload}, {put, call}) {
            const res = yield call(servicesList.fetchCity);
            if(!res){
                return;
            }
            yield put({
                type: 'fetchedCity',
                payload: res.data,
            })
        },

    },
    reducers: {
        gotProgress(state, {payload}) {
            let progress = Math.floor(+payload.process);
            let hotelMount = payload.hotelCount;
            return {
                ...state,
                progress,
                hotelMount,
                loading: false,
            }
        },

        fetchedCity(state, {payload}) {
            return {
                ...state,
                cityData: payload,
            }
        },

        searched(state, {payload}) {
            return {
                ...state,
                showProgress: true,
                progress: null,
                loading: true,
                hotelMount: 0,
            }

        },
        handleSelect(state, {payload}) {
            return {
                ...state,
                selectedCity: payload,
            }
        }

    },
    actions: {
        searchProgress(payload) {
            return {
                type: 'crawl/searchProgress',
                payload: payload,
            }
        },
        handleSearch(payload) {
            return {
                type: 'crawl/handleSearch',
                payload: payload,
            }
        },
        handleSelect(payload) {
            return {
                type: 'crawl/handleSelect',
                payload: payload,
            }
        },
        fetchCity() {
            return {
                type: 'crawl/fetchCity',
            }
        },
    }
};
