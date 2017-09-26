import React from 'react';
import { Menu as AntdMenu } from 'antd';
import MenuItem from './item';

/**
 * menu tree
 *
 * @param {any} { data, ...menuProps }
 * @param {Array} data menu list
 * @returns {React.DOM}
 */
const Menu = ({ data, ...menuProps }) => (
    <AntdMenu {...menuProps}>
        { data ? data.map((item, index) => (
            <MenuItem key={index} {...item} />
            )) : null }
    </AntdMenu>
);

export default Menu;
