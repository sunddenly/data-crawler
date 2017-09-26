import React from 'react';
import PropTypes from 'prop-types';
import { Layout, Icon } from 'antd';
import classnames from 'classnames';
import Menu from '../menu';
import clsWrapper from '../../utils/react/cls-wrapper';
import './index.less';

const { Header, Sider, Content } = Layout;

/**
 * Sider layout
 *
 * @param {boolean} collapsed
 * @param {string|React.DOM} logo
 * @param {array} headerMenus
 * @param {array} siderMenus
 * @param {React.DOM|string} [optinal] headerContent
 * @param {React.DOM|string} [optional] siderContent
 * @param {Menu.propTypes} [optinal] headerProps
 * @param {Menu.propTypes} [optinal] siderProps
 * @param {Function} onCollapse
 * @returns
 */
const SiderLayout = (props) => {
    const {
        collapsed,
        logo,
        headerMenus,
        headerMenuProps,
        headerContent,
        siderMenus,
        siderMenuProps,
        siderContent,
        onCollapse,
        children,
    } = props;

    return (
        <Layout
            className={classnames({
                layout: true,
                'layout-collapsed': collapsed,
            })}
        >
            <Sider
                trigger={null}
                className="layout-sider"
                collapsible
                collapsed={collapsed}
                onCollapse={onCollapse}
            >
                <div className="siderlayout-logo">{logo}</div>
                <Menu
                    className="layout-sider-menu"
                    mode={collapsed ? 'vertical' : 'inline'}
                    data={siderMenus}
                    {...siderMenuProps}
                />
                {siderContent}
            </Sider>
            <Layout>
                <Header className="cf">
                    <Icon
                        className="layout-sider-trigger"
                        type={collapsed ? 'menu-unfold' : 'menu-fold'}
                        onClick={onCollapse}
                    />
                    <div className="layout-title cf">
                        <Icon className="fr" type='user' style={{fontSize: 16, marginTop: 15}}/>
                    </div>
                </Header>
                <Content>{children}</Content>
            </Layout>
        </Layout>
    );
};

SiderLayout.defaultProptypes = {
    collapsed: false,
};

/**
 * container with margin
 */
export const Contaienr = SiderLayout.Container = clsWrapper('layout-container');

/**
 * contaienr with box shadow and white bg
 */
export const Page = SiderLayout.Page = clsWrapper('layout-page');

/**
 * container with margin and box-shadow, white bg
 */
export const PageContainer = SiderLayout.PageContainer = ({ children }) => (
    <Contaienr>
        <Page>
            {children}
        </Page>
    </Contaienr>
);

export default SiderLayout;
