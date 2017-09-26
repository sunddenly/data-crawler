import React from 'react';
import { Menu as AntdMenu } from 'antd';
import { Link } from 'dva/router';
import MenuTitle from './title';

const AntdSubMenu = AntdMenu.SubMenu;
const AntdMenuItem = AntdMenu.Item;

/**
 * menu item generator
 *
 * @param {string} menuKey unique key for each menu
 * @param {string} title
 * @param {string} url
 * @param {string} icon
 * @param {Array<MenuItem.propTypes>} [optional] submenus
 * @param {string} [optional] target if target exsits, it renders a link.
 *          Otherwise, it turns to router link.
 * @param {boolean} [optional] disabled
 * @param {string} [optional] type ['divider']
 * @returns {React.DOM}
 */
const MenuItem = (props) => {
    const {
        menuKey,
        title,
        url,
        target,
        icon,
        disabled,
        type,
        submenus,
        ...restProps
    } = props;

    const menuTitle = (
        <MenuTitle icon={icon} title={title} />
    );

    if (submenus) {
        return (
            <AntdSubMenu
                key={menuKey}
                title={menuTitle}
                disabled={disabled}
                {...restProps}
            >
                {submenus.map(({ menuKey, ...rest }) => (
                    <MenuItem key={menuKey} {...rest} />
                ))}
            </AntdSubMenu>
        );
    } else if (type === 'divider') {
        return (
            <AntdMenu.Divider />
        );
    }

    return (
        <AntdMenuItem
            key={menuKey}
            disabled={disabled}
            {...restProps}
        >
            {target ? (
                <a target={target} href={url}>
                    {menuTitle}
                </a>
            ) : (
                <Link to={url}>
                    {menuTitle}
                </Link>
            )}
        </AntdMenuItem>
    );
};

export default MenuItem;
