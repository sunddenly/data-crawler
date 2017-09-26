import React from 'react';
import { Icon } from 'antd';

/**
 * menu title
 *
 * @param {any} { icon, title }
 * @returns {React.DOM}
 */
const MenuTitle = ({ icon, title }) => (
    <span>
        {icon ? <Icon type={icon} /> : null}
        <span className="layout-menu-text">{title}</span>
    </span>
);

export default MenuTitle;
