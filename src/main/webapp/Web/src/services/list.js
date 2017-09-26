import request from '../utils/request';

export const fetchCity = () => request({
    method: 'GET',
    url: '/spider/city/list',
    successTips: '城市数据加载成功',

})

export const fetchHotel = ({cityId, pageNumber}) => request({
    method: 'GET',
    url: `/spider/hotel/${cityId}/${pageNumber}`,
})

export default {
    fetchCity,
    fetchHotel
}
