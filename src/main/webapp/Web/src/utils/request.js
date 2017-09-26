import fetch from 'dva/fetch';
import { message } from 'antd';
import { isNullOrUndefined, toSearchStr } from './index';


// 请求统一入口
export default function (props) {
    /* eslint-disable */
    let {
        url,
        method = 'GET',
        data = {},
        successTips = null,
        failTips = '操作失败，请刷新试试',
        check = res => (res.code === 200),
        dataType = 'json',
    } = props;
    /* eslint-enable */

    //url = `http://10.66.0.208:8080${url}`;
    //url = `${url}`

    const reqParams = {
        headers: {
        },
        mode: 'cors',
    };

    reqParams.method = method.toUpperCase();

    if (reqParams.method === 'GET') {
        url += toSearchStr(data);
    }

    if (['GET'].indexOf(reqParams.method) === -1) {
        /* eslint-disable */
        if (data instanceof FormData) {
        /* eslint-enable */
            reqParams.body = data;
            delete reqParams.headers['Content-Type'];
        } else {
            reqParams.body = JSON.stringify(data);
            reqParams.headers['Content-Type'] = 'application/json';
        }
    }

    reqParams.credentials = 'include';

    return fetch(url, reqParams)
        .then(res => {
            return res.json();
        })
        .then((res) => {
            if (check(res)) {
                if (!isNullOrUndefined(successTips)) {
                    message.info(successTips, 0.5);
                }
            } else {
                message.error(res.err);
                return;
            }

            return res;
        }).catch((err) => {
            message.error(isNullOrUndefined(failTips) ? err : failTips);
        });

    }
