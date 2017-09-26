import logo from '../assets/images/logo.png';
import { isNullOrUndefined } from '../utils';

export default {
    namespace: 'adminLayout',
    state: {
        siderMenus: [
            {
                menuKey: 'menu1',
                title: '数据展示',
                icon: 'smile',
                url: '/list',
            },
            {
                menuKey: 'menu2',
                title: '数据爬取',
                icon: 'smile-o',
                url: '/crawl',
            }
        ],
        menuCollapsed: false,
        logoTitle : 'crawler',
        logo,
    },
    reducers: {
        changeMenuCollapsed(state, { payload }) {
            return {
                ...state,
                menuCollapsed: isNullOrUndefined(payload) ? !state.menuCollapsed : payload,
            };
        },
    },
};
