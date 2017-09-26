import services from '../services/list';

export default {
    namespace : 'list',
    state : {
        cityData: [
        ],
        hotelData: null,
        lastUpdateTime: null,
        loading: false,
        hotelDetail: {},
        selectedCity: null,
        totalCount: null,
        totalPage: null,
    },
    effects: {
        * fetchCity({payload}, {put, call}) {
            const res = yield call(services.fetchCity);
            if(!res){
                return;
            }

            yield put({
                type: 'fetchedCity',
                payload: res.data,
            })
        },

        *fetchHotel({payload}, {put, call}) {
            yield put({
                type: 'toggleLoading',
            })
            const res = yield call(services.fetchHotel, payload);
            if(!res) {
                return;
            }
            yield put({
                type: 'fetchedHotel',
                payload: res.data,
                city: payload,
            })
            yield put({
                type: 'toggleLoading',
            })
        }

    },
    reducers: {
        fetchedCity(state, {payload}) {
            return {
                ...state,
                cityData: payload,
            }
        },

        fetchedHotel(state, {payload, city}) {
            const hotelData = payload.hotelItemVos;
            const lastUpdateTime = payload.newUpdateTime;
            const selectedCity = city.cityId;
            const totalCount = payload.totalCount;
            const totalPage = payload.totalPage;
            return {
                ...state,
                lastUpdateTime,
                hotelData,
                selectedCity,
                totalCount,
                totalPage,
            }
        },

        toggleLoading(state, {payload}) {
            return {...state, loading: !state.loading}
        },

        fetchHotelDetail(state, {payload}) {
            let hotelDetail = state.hotelDetail;
            let id = payload;
            if(hotelDetail[id]) {
                return {...state}
            }
            else {
                const detail = [];
                for(let i = 0; i < 20; i++){
                    let item = {};
                    item['id'] = id  + '' + i;
                    item['type'] = '大床房' + i;
                    item['bedType'] = '双人床';
                    item['freeCancle'] = '支持';
                    item['payWay'] = '在线付款';
                    item['price'] = 100 + i;
                    detail.push(item);
                }
                hotelDetail[id] = detail;
                return {
                    ...state,
                    hotelDetail,
                }
            }
        }
    },
    actions: {
        fetchCity() {
            return {
                type: 'list/fetchCity',
            }
        },
        fetchHotel(payload) {
            return {
                type: 'list/fetchHotel',
                payload: payload,
            }
        },
        fetchHotelDetail(payload) {
            return {
                type: 'list/fetchHotelDetail',
                payload: payload,
            }
        }
    }
};
