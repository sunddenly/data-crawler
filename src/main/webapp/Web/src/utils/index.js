
export const getObjValue = (obj, attr) => {
    attr = attr.split('.');
    while (attr.length) {
        obj = obj[attr.pop()];
    }
    return obj;
};

export const objCompare = (one, another, props) =>
    props.some(prop => getObjValue(one, prop) === getObjValue(another, prop));

/**
 * isNullOrUndefined
 *
 * @export
 * @param {any} obj
 * @returns {boolean} isNullOrUndefined
 */
export function isNullOrUndefined(obj) {
    return typeof obj === 'undefined' || obj === null;
}

/**
 * 深度查找
 *
 * @export
 * @param {any} data 数据源
 * @param {any} value 匹配值
 * @param {boolean} [findOne=true] 是否找到一个就返回
 * @param {string} [key='id'] id
 * @param {string} [childKey='children'] 子集key
 * @returns {any|Array} matches findOne[true] => any, findOne[false] => Array
 */
export function findRecursive(data, value, findOne = true, key = 'id', childKey = 'children') {
    if (!data) {
        return null;
    }
    const finded = [];
    let stack = [{
        parent: null,
        data: {
            [key]: null,
            children: data,
        },
    }];
    while (stack.length) {
        const ele = stack.pop();
        const data = ele.data;
        if ((typeof value === 'function' && value(data)) ||
            (!isNullOrUndefined(value) && value === data[key])) {
            finded.push(ele);
            if (findOne) {
                break;
            }
        }
        const children = data[childKey];
        if (children && children.length) {
            stack = stack.concat(children.map(child => ({
                parent: ele,
                data: child,
            })));
        }
    }
    return finded.length === 0 ? null : findOne ? finded[0] : finded;
}

export const groupBy = (arr, attrname, getKey) => {
    const grouped = {};
    arr.forEach((ele) => {
        let val = getObjValue(ele, attrname);

        if (typeof getKey === 'function') {
            val = getKey(val);
        }

        if (isNullOrUndefined(grouped[val])) {
            grouped[val] = [];
        }
        grouped[val].push(ele);
    });
    return grouped;
};

export function toSearchStr(data) {
    const queryString = Object.keys(data).map((key) => {
        const val = data[key];
        if (isNullOrUndefined(val)) {
            return null;
        }
        if (Array.isArray(val)) {
            return val.map(v => `${key}=${v}`).join('&');
        }
        return `${key}=${val}`;
    }).filter(item => item !== null).join('&');
    return queryString.length === 0 ? '' : `?${queryString}`;
}

export const pick = (obj, keys, filter) => {
    const newObj = {};
    keys.forEach((key) => {
        const val = obj[key];
        if (typeof filter === 'function' && !filter(val, key, obj)) {
            return;
        }
        newObj[key] = val;
    });
    return newObj;
};

export const find = (arr, test, findOne) => {
    let item;
    let matched = [];
    for (let i = 0, len = arr.length; i < len; i++) {
        item = arr[i];
        if (test(item)) {
            matched.push({
                index: i,
                value: item,
            });
            if (findOne) {
                break;
            }
        }
    }
    if (findOne) {
        matched = matched[0];
    }
    return matched;
};

export function omit(obj, keys) {
    const newObj = {};
    /* eslint-disable */
    for (const key in obj) {
        if (keys.indexOf(key) === -1 && Object.prototype.hasOwnProperty.call(obj, key)) {
            newObj[key] = obj[key];
        }
    }
    /* eslint-enables */
    return newObj;
}

export const randomStr = length => Math.random().toString(36).substring(2, 2 + length);


export const sortJson = (json, onVal, onIgnore) => {
    const sortedJson = {};
    const keys = Object.keys(json)
        .sort();
    keys.forEach(key => {
        let val = json[key];
        if (typeof onIgnore === 'function' && onIgnore(val, key, json)) {
            return;
        }
        if (!Array.isArray(val) && val !== null
            && typeof val === 'object') {
            val = sortJson(val, onVal, onIgnore);
        }
        if (typeof onVal === 'function') {
            val = onVal(val, key, json);
        }
        sortedJson[key] = val;
    });
    return sortedJson;
};
