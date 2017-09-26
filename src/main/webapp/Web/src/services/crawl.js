import request from '../utils/request';

// export const fetchCity = () => request({
//     method: 'GET',
//     url: '/spider/city/list',
//     successTips: '城市数据加载成功',

// })

export const start = (payload) => request({
    method: 'POST',
    url: '/spider/crawling/start',
    data: {
        cityId: payload,
    }
})

export const getProgress = (cityId) => request({
    method: 'GET',
    url: `/spider/crawling/status/${cityId}`,
})


export default {
    start,
    getProgress,
}
